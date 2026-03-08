struct Colors {
	static constexpr const char* RESET = "\033[0m";
	static constexpr const char* RED = "\033[31m";
	static constexpr const char* GREEN = "\033[32m";
	static constexpr const char* YELLOW = "\033[33m";
	static constexpr const char* BLUE = "\033[34m";
	static constexpr const char* MAGENTA = "\033[35m";
	static constexpr const char* CYAN = "\033[36m";
	static constexpr const char* WHITE = "\033[37m";
};


#pragma once
enum TilesType : short {
	PathTile, PlayerTile, StartTile, EndTile, WallTile, DarknessTile, VoidTile, UnknownTile
};
const char DEFAULT_SETTINGS[8] = { '.', '@', 'S', 'E', '#', '~', ' ', '?' };

struct TilesSettings {
	char settings[8];
	TilesSettings() {
		SetDefaultSettings();
	}
	void SetDefaultSettings() {
		for (int i = 0; i < 8; i++) settings[i] = DEFAULT_SETTINGS[i];
	}

	char operator[](TilesType tt) const {
		return settings[tt];
	}

	void Setting(TilesType tt, char new_symbol) {
		settings[tt] = new_symbol;
	}
}; 



enum TilesColor : short {
	Red, Yellow, Orange, Green, Blue, Indigo, Purple
};

