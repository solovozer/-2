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
        
        k.Label(root, text="Distance:").grid(row=0, column=0, padx=10, sticky="e")
        dist_var = tk.DoubleVar()
        dist_scale = tk.Scale(root, from_=0, to=500, orient="horizontal", variable=dist_var, length=200)
        dist_scale.grid(row=0, column=1, padx=10, pady=5)

        tk.Label(root, text="Weight:").grid(row=1, column=0, padx=10, sticky="e")
        weight_var = tk.DoubleVar()
        weight_scale = tk.Scale(root, from_=0, to=100, orient="horizontal", variable=weight_var, length=200)
        weight_scale.grid(row=1, column=1, padx=10, pady=5)

        self.root.update()
        self.draw_package()

    def update_package(self, event):
        self.draw_package()

    def draw_package(self): 
        k.draw_refined_package(self.canvas, self.scale_var.get())

if __name__ == "__main__":
    root = tk.Tk()
    app = PackagingApp(root)
    root.mainloop()