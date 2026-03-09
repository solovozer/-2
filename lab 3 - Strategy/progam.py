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
        self.root.rowconfigure(0, weight=4)    
        self.root.rowconfigure(1, weight=1)    

        # Frame 0 : Box
        self.box_frame = tk.Frame(self.root, bg="white", bd=1, relief="solid")
        self.box_frame.grid(row=0, column=0, sticky="nsew", padx=10, pady=10)
        self.canvas = tk.Canvas(self.box_frame, bg="white", highlightthickness=0)
        self.canvas.pack(expand=True, fill="both")
                
        # Frame 1 : Info
        self.stuff_frame = tk.LabelFrame(self.root, text=" Data ")
        self.stuff_frame.grid(row=0, column=1, sticky="nsew", padx=10, pady=10)
        tk.Label(self.stuff_frame, text="<< Stuff 1 >>", font=("Arial", 12)).pack(pady=20)

        #Frame 2: Slider
        self.slider_frame = tk.Frame(self.root)
        self.slider_frame.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)
        self.scale_var = tk.DoubleVar(value=1.0)
        self.slider = ttk.Scale(self.slider_frame, from_=0.5, to=2.0, variable=self.scale_var, 
                                orient="horizontal", command=self.draw_package)
        self.slider.pack(fill="x", side="bottom")
        tk.Label(self.slider_frame, text="<< Slider to change box scale >>").pack(side="bottom")
        
        # Frame 3: Save
        self.button_frame = tk.Frame(self.root)
        self.button_frame.grid(row=1, column=1, sticky="nsew", padx=10, pady=10)
        ttk.Button(self.button_frame, text="<< Button >>", command=lambda: print("Action!")).pack(expand=True)

    def draw_package(): k.draw_refined_package(PackagingApp, 1)

if __name__ == "__main__":
    root = tk.Tk()
    app = PackagingApp(root)
    root.mainloop()