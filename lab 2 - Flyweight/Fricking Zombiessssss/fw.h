#pragma once
#include <raylib.h>
#include <raymath.h>
#include <unordered_map>
#include <vector>
#include <string>
#include <sstream>
#include <iomanip>


using namespace std;

enum TYPES {
    ZOMBIE,
    HUMAN
};

class CharacterTexture {
public:
    RenderTexture2D renderTex;
    CharacterTexture() { renderTex = LoadRenderTexture(40, 40); }
    virtual ~CharacterTexture() { UnloadRenderTexture(renderTex); }
    virtual void Generate() = 0;
};

class ZombieTexture : public CharacterTexture {
public:
    ZombieTexture() : CharacterTexture() { Generate(); }

    void Generate() override {
        int size = 40;
        Color color = LIME;
        BeginTextureMode(renderTex);
        ClearBackground(BLANK);
        float c = size / 2.0f;
        DrawCircle(c + 12, c - 8, 5, color); // Hands
        DrawCircle(c + 12, c + 8, 5, color);
        DrawCircle(c, c, 12, color);         // Body
        DrawCircle(c + 4, c - 3, 2, RED);    // Eyes
        DrawCircle(c + 4, c + 3, 2, RED);    // Eyes
        EndTextureMode();
    }
};

class PlayerTexture : public CharacterTexture {
public:
    PlayerTexture() { Generate(); }

    void Generate() override {
        int size = 40;
        Color color = BLUE;
        BeginTextureMode(renderTex);
        ClearBackground(BLANK);
        float c = size / 2.0f;
        DrawCircle(c, c, size / 3.0f, color);        // Body
        EndTextureMode();
    }
};

class TextureFactory {
    unordered_map<TYPES, CharacterTexture*> registry;
public:
    CharacterTexture* GetTexture(TYPES pt) {
        if (registry.find(pt) == registry.end()) {
            switch (pt) {
            case TYPES::HUMAN: registry[pt] = new PlayerTexture(); break;
            case TYPES::ZOMBIE: registry[pt] = new ZombieTexture(); break;
            }
        }
        return registry[pt];
    }

    ~TextureFactory() {
        for (auto& pair : registry) delete pair.second;
    }
};

TextureFactory factory;

class Character {
public:
    Vector2 pos;
    float rot = 0;
    float radius = 10.0f;
    TYPES type;

    Character(Vector2 p, TYPES tp) : pos(p), type(tp) {}

    void Draw() {
        Rectangle src = { 0, 0, (float)factory.GetTexture(type)->renderTex.texture.width, (float)-factory.GetTexture(type)->renderTex.texture.height };
        Rectangle dest = { pos.x, pos.y, (float)factory.GetTexture(type)->renderTex.texture.width, (float)factory.GetTexture(type)->renderTex.texture.height };
        Vector2 origin = { (float)factory.GetTexture(type)->renderTex.texture.width / 2, (float)factory.GetTexture(type)->renderTex.texture.height / 2 };
        DrawTexturePro(factory.GetTexture(type)->renderTex.texture, src, dest, origin, rot, WHITE);
    }
};

class Player : public Character {
public:
    int hp = 100; 
    int maxHp = 100;
    float damageCooldown = 0;

    Player(Vector2 p = {0, 0}) : Character(p, TYPES::HUMAN) {}

    void Update(Vector2 worldMouse) {
        if (hp <= 0) return;

        if (IsKeyDown(KEY_W)) pos.y -= 4;
        if (IsKeyDown(KEY_S)) pos.y += 4;
        if (IsKeyDown(KEY_A)) pos.x -= 4;
        if (IsKeyDown(KEY_D)) pos.x += 4;

        Vector2 dir = Vector2Subtract(worldMouse, pos);
        rot = atan2f(dir.y, dir.x) * RAD2DEG;

        if (damageCooldown > 0) damageCooldown -= GetFrameTime();
    }

    void DrawHealthBar() {
        DrawRectangle(pos.x - 20, pos.y - 30, 40, 5, RED);
        float healthWidth = 40.0f * ((float)hp / maxHp);
        DrawRectangle(pos.x - 20, pos.y - 30, (int)healthWidth, 5, GREEN);
    }
};

class Zombie : public Character {
    Player* target;
public:
    Zombie(Vector2 p, Player* targetObj)
        : Character(p, TYPES::ZOMBIE), target(targetObj) {
    }

    void Update() {
        Vector2 dir = Vector2Subtract(target->pos, pos);
        rot = atan2f(dir.y, dir.x) * RAD2DEG;
        if (Vector2Length(dir) > 5) {
            pos = Vector2Add(pos, Vector2Scale(Vector2Normalize(dir), 1.5f));
        }
    }
};

class DebugOverlay {
public:
    void Draw(int textureCount, int textureSize, vector<Zombie>& horde) {
        int fps = GetFPS();
        float vramUsed = (textureCount * (textureSize * textureSize * 4 * 2)) / 1024.0f;

        std::stringstream ss;
        ss << horde.size() << "Zombies | VRAM : " << std::fixed << std::setprecision(2) << vramUsed << " KB" << " | FPS : " << fps;
        std::string debugText = ss.str();
        int fontSize = 20;
        int padding = 10;
        int textWidth = MeasureText(debugText.c_str(), fontSize);

        int xPos = GetScreenWidth() - textWidth - padding;
        int yPos = GetScreenHeight() - fontSize - padding;
        DrawRectangle(xPos - 5, yPos - 5, textWidth + 10, fontSize + 10, Fade(BLACK, 0.6f));
        DrawText(debugText.c_str(), xPos, yPos, fontSize, LIME);
    }
};

class Game {
private:
    DebugOverlay debug;
    Camera2D camera;
    Player player;
    vector<Zombie> horde;
    int Failsafe = 0;
    bool started = false;
public:
    Game()
    {
        InitWindow(800, 600, "FricKing Zombies - fw");
        SetTargetFPS(60);

        camera.target = player.pos;
        camera.offset = { GetScreenWidth() / 2.0f, GetScreenHeight() / 2.0f };
        camera.rotation = 0.0f;
        camera.zoom = 1.0f;
    }

    ~Game() {
        CloseWindow();
    }

    void Run() {
        while (!WindowShouldClose()) {
            if (!started) StartScreen();
            Update();
            Draw();
        }
    }

private:
    void ResolveZombieCollisions(std::vector<Zombie>& horde) {
        for (size_t i = 0; i < horde.size(); i++) {
            for (size_t j = i + 1; j < horde.size(); j++) {
                float dist = Vector2Distance(horde[i].pos, horde[j].pos);
                float minSafeDist = horde[i].radius + horde[j].radius;

                if (dist < minSafeDist) {
                    Vector2 normal = Vector2Normalize(Vector2Subtract(horde[i].pos, horde[j].pos));
                    float overlap = minSafeDist - dist;

                    Vector2 push = Vector2Scale(normal, overlap * 0.5f);
                    horde[i].pos = Vector2Add(horde[i].pos, push);
                    horde[j].pos = Vector2Subtract(horde[j].pos, push);
                }
            }
        }
    }

    void StartScreen() {
        while (!IsKeyPressed(KEY_J)) {
            BeginDrawing();
            const char* title = "FricKing Zombies";
            int titleFontSize = 60;
            int titleWidth = MeasureText(title, titleFontSize);
            DrawText(title, GetScreenWidth() / 2 - titleWidth / 2, GetScreenHeight() / 2 - 80, titleFontSize, RED);
            const char* instruction = "Press J to start";
            int instructionFontSize = 20;
            int instructionWidth = MeasureText(instruction, instructionFontSize);
            float blink = (int)(GetTime() * 2) % 2;
            Color instructionColor = (blink == 0) ? GRAY : RAYWHITE;
            DrawText(instruction, GetScreenWidth() / 2 - instructionWidth / 2, GetScreenHeight() / 2 + 30, instructionFontSize, instructionColor);
            EndDrawing();
        }
        started = true;
    }

    void Update() {
        Vector2 worldMouse = GetScreenToWorld2D(GetMousePosition(), camera);
        player.Update(worldMouse);

        camera.target.x += (player.pos.x - camera.target.x) * 0.1f;
        camera.target.y += (player.pos.y - camera.target.y) * 0.1f;

        if (IsKeyPressed(KEY_P)) Failsafe = ~Failsafe;

        int vx = GetRandomValue(-300, 300);
        int vy = GetRandomValue(-300, 300);

        if (!Failsafe) {
            if (player.hp > 0) horde.push_back(Zombie({ 100 + (float)vx, 100 + (float)vy }, &player));
        }
        else {
            horde.clear();
        }

        for (auto& z : horde) {
            z.Update();
            if (CheckCollisionCircles(player.pos, player.radius, z.pos, z.radius)) {
                if (player.damageCooldown <= 0) {
                    player.hp -= 10;
                    player.damageCooldown = 0.5f;
                }
            }
        }
        ResolveZombieCollisions(horde);
    }

    void Draw() {
        BeginDrawing();
        ClearBackground(BLACK);
        BeginMode2D(camera);
        for (auto& z : horde) z.Draw();

        if (player.hp > 0) {
            player.Draw();
            player.DrawHealthBar();
        }
        EndMode2D();
        if (player.hp <= 0) {
            DrawRectangle(0, 0, GetScreenWidth(), GetScreenHeight(), Fade(BLACK, 0.8f));
            DrawText("YOU LOSE", GetScreenWidth() / 2 - 100, GetScreenHeight() / 2 - 20, 40, RED);
            DrawText("Press R to restart", GetScreenWidth() / 2 - 80, GetScreenHeight() / 2 + 30, 20, RAYWHITE);
            
            if (horde.size() > 0) horde.clear();
            if (IsKeyPressed(KEY_R)) {
                player.hp = 100;
            }
        }
        debug.Draw(3, 40, horde);
        EndDrawing();
    }
};