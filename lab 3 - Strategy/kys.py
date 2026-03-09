import tkinter as tk
import numpy as np

def draw_refined_package(parent, scale):
    frame = tk.Frame(parent, bg="white")
    canvas = tk.Canvas(frame, width=600, height=600, bg="white", highlightthickness=0)
    canvas.pack(expand=True, fill="both")

    O = np.array([250, 400]) 
    PI = np.pi
    
    width = 120 * scale
    length = 180 * scale
    height = 150 * scale

    tape_width = width / 3
    tape_height = height / 6 

    l = np.array([-np.cos(PI/6), np.sin(PI/6)])
    r = np.array([np.cos(PI/6), np.sin(PI/6)])
    u = np.array([0, -1])

    c_top, c_left, c_right, c_tape = "#d9bf77", "#c9af67", "#ba9f57", "#a88e46"

    def to_pts(*args): 
        pts = []
        for pt in args: pts.extend([pt[0], pt[1]])
        return pts

    bottom_left  = O + (l * length)
    bottom_right = O + (r * width)
    bottom_front = O + (l * length) + (r * width)
    
    top_center   = O + (u * height)
    top_left     = bottom_left + (u * height)
    top_right    = bottom_right + (u * height)
    top_front    = bottom_front + (u * height)

    tape_center_front = (top_left + top_front) / 2
    tape_top_left = tape_center_front - (r * (tape_width / (2 * np.cos(PI/6))))
    tape_top_right = tape_center_front + (r * (tape_width / (2 * np.cos(PI/6))))
    tape_down_left = tape_top_left - (u * tape_height)
    tape_down_right = tape_top_right - (u * tape_height)
    tape_up_right = tape_top_left - (l * length)
    tape_up_left = tape_top_right - (l * length)
    
    text_pos = (bottom_front + top_right) / 2
    
    canvas.create_polygon(to_pts(bottom_left, bottom_front, top_front, top_left), fill=c_left)
    canvas.create_polygon(to_pts(bottom_right, bottom_front, top_front, top_right), fill=c_right)
    canvas.create_polygon(to_pts(top_center, top_left, top_front, top_right), fill=c_top)
    canvas.create_polygon(to_pts(tape_top_left, tape_top_right, tape_down_right, tape_down_left), fill=c_tape)
    canvas.create_polygon(to_pts(tape_up_left, tape_up_right, tape_top_left, tape_top_right), fill=c_tape)
    canvas.create_text(text_pos[0], text_pos[1], text="PACKAGE", angle=-30, font=("Arial", int(16*scale), "bold"), fill="#333333")

    return frame