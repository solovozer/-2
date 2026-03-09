import tkinter as tk
from tkinter import ttk
import kys as k

class PackagingApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Packaging Inspector UI")
        self.root.geometry("800x600")
        
        self.root.columnconfigure(0, weight=2)
        self.root.columnconfigure(1, weight=1) 
        self.root.rowconfigure(0, weight=1)       

        self.box_frame = tk.Frame(self.root, bg="white", bd=1, relief="solid")
        self.box_frame.grid(row=0, column=0, sticky="nsew", padx=10, pady=(10, 0))
        
        self.canvas = tk.Canvas(self.box_frame, bg="white", highlightthickness=0)
        self.canvas.pack(expand=True, fill="both")
        self.stuff_frame = tk.LabelFrame(self.root, text=" Data ")
        self.stuff_frame.grid(row=0, column=1, rowspan=2, sticky="nsew", padx=10, pady=10)
        tk.Label(self.stuff_frame, text="<< Package Info >>", font=("Arial", 12)).pack(pady=20)
        
        tk.Label(self.stuff_frame, text="Distance:").pack(pady=5)
        self.dist_var = tk.DoubleVar()
        self.dist_scale = tk.Scale(self.stuff_frame, from_=0, to=500, orient="horizontal", variable=self.dist_var, length=200)
        self.dist_scale.pack(pady=5)

        tk.Label(self.stuff_frame, text="Weight:").pack(pady=5)
        self.weight_var = tk.DoubleVar()
        self.weight_scale = tk.Scale(self.stuff_frame, from_=0, to=100, orient="horizontal", variable=self.weight_var, length=200)
        self.weight_scale.pack(pady=5)

        self.dist_scale.bind("<Motion>", self.update_package)
        self.weight_scale.bind("<Motion>", self.update_package)

        self.root.update()
        self.draw_package()

    def update_package(self, event):
        self.draw_package()

    def draw_package(self): 
        scale = self.weight_scale.get() / 10.0  # Adjust scale based on weight
        k.draw_refined_package(self.canvas, scale)

if __name__ == "__main__":
    root = tk.Tk()
    app = PackagingApp(root)
    root.mainloop()