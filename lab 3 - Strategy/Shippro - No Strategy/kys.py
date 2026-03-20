import numpy as np

PI = np.pi
cos30 = np.cos(PI/6)
sin30 = np.sin(PI/6)


def draw_refined_package(canvas, scale):
    r_scale = (scale + 13) / 14 
    width, length, height = 120 * r_scale, 180 * r_scale, 150 * r_scale
    
    total_h_2d = (length + width) * sin30 + height

    offset_x = (length - width) * cos30 / 2
    offset_y = (total_h_2d / 2) - (height)

    canvas.delete("all") 
    cx = canvas.winfo_width() / 2 + offset_x
    cy = canvas.winfo_height() / 2 + offset_y
    O = np.array([cx, cy + (50 * r_scale)])
    l = np.array([-cos30, sin30])
    r = np.array([cos30, sin30])
    u = np.array([0, -1])

    c_top, c_left, c_right, c_tape = "#d9bf77", "#c9af67", "#ba9f57", "#a88e46"

    def to_pts(*args): 
        pts = []
        for pt in args: pts.extend([pt[0], pt[1]])
        return pts

    bottom_left  = O + (l * length)
    bottom_right = O + (r * width)
    bottom_front = O + (l * length) + (r * width)
    top_left     = bottom_left + (u * height)
    top_right    = bottom_right + (u * height)
    top_front    = bottom_front + (u * height)
    top_center   = O + (u * height)

    canvas.create_polygon(to_pts(bottom_left, bottom_front, top_front, top_left), fill=c_left, outline="black")
    canvas.create_polygon(to_pts(bottom_right, bottom_front, top_front, top_right), fill=c_right, outline="black")
    canvas.create_polygon(to_pts(top_center, top_left, top_front, top_right), fill=c_top, outline="black")

    text_pos = (top_left + bottom_front) / 2
    canvas.create_text(text_pos[0], text_pos[1], text="PACKAGE", angle=30, font=("Arial", int(12*r_scale), "bold"), fill="#333333")