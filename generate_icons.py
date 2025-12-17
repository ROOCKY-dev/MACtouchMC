from PIL import Image, ImageDraw
import os

ICON_SIZE = (16, 16)
OUTPUT_DIR = "src/main/resources/assets/mactouchmc/icons"

# Color Palette (Cyberpunk/Dark Mode friendly)
BG_COLOR = (0, 0, 0, 0) # Transparent
FG_COLOR = (255, 255, 255, 255) # White
ACCENT_COLOR = (0, 255, 200, 255) # Cyan/Teal
DISABLED_COLOR = (100, 100, 100, 255) # Grey

def create_image(name, draw_func, disabled=False):
    img = Image.new("RGBA", ICON_SIZE, BG_COLOR)
    draw = ImageDraw.Draw(img)
    color = DISABLED_COLOR if disabled else FG_COLOR
    draw_func(draw, color)
    
    filename = f"{name}_disabled.png" if disabled else f"{name}.png"
    filepath = os.path.join(OUTPUT_DIR, filename)
    img.save(filepath)
    print(f"Generated {filepath}")

# Drawing Functions
def d_camera(draw, color): # screenshot, cycle_camera
    draw.rectangle([2, 4, 13, 11], outline=color)
    draw.rectangle([6, 2, 9, 4], fill=color)
    draw.ellipse([5, 5, 10, 10], outline=color)

def d_debug(draw, color): # debug_screen
    draw.rectangle([2, 2, 13, 13], outline=color)
    draw.line([2, 2, 13, 13], fill=color)
    draw.line([13, 2, 2, 13], fill=color)

def d_chat(draw, color): # f3_clear_chat
    draw.rectangle([2, 3, 13, 10], outline=color)
    draw.polygon([(4, 10), (4, 13), (7, 10)], fill=color)

def d_copy(draw, color): # f3_copy_data, f3_copy_location
    draw.rectangle([4, 4, 12, 13], outline=color) # Front paper
    draw.rectangle([6, 2, 14, 11], outline=color) # Back paper offset (simplified)

def d_game_mode(draw, color): # f3_cycle_gamemode
    draw.ellipse([2, 2, 13, 13], outline=color)
    draw.polygon([(5, 5), (10, 5), (7, 10)], fill=color)

def d_view_distance(draw, color): # f3_cycle_render_distance
    draw.rectangle([1, 6, 14, 9], outline=color) # horizon
    draw.polygon([(1, 9), (4, 4), (7, 9)], outline=color) # mountain 1
    draw.polygon([(6, 9), (10, 3), (14, 9)], outline=color) # mountain 2

def d_reload(draw, color): # f3_reload_chunks, f3_reload_resource_packs
    draw.arc([2, 2, 13, 13], 45, 315, fill=color)
    draw.polygon([(10, 2), (13, 5), (10, 8)], fill=color) # Arrow head

def d_boxes(draw, color): # f3_show_chunk_boundaries, f3_show_hitboxes
    draw.rectangle([2, 2, 13, 13], outline=color)
    draw.line([2, 2, 13, 13], fill=color) # Only line? No, crossed box or grid
    if "chunk" in str(draw_func): # Hacky context check or just simple grid
         draw.line([7, 2, 7, 13], fill=color)
         draw.line([2, 7, 13, 7], fill=color)

def d_pause(draw, color): # f3_pause, pause_stream
    draw.rectangle([4, 3, 6, 12], fill=color)
    draw.rectangle([9, 3, 11, 12], fill=color)

def d_tooltips(draw, color): # f3_advanced_tooltips, f3_help
    draw.text((4, 2), "?", fill=color) # Simple text fallback? Or shape
    draw.ellipse([2, 2, 13, 13], outline=color)
    draw.line([7, 4, 7, 9], fill=color)
    draw.point([7, 11], fill=color)

def d_hud(draw, color): # toggle_hud, f3_toggle_auto_pause
    draw.rectangle([2, 2, 13, 13], outline=color)
    draw.rectangle([4, 10, 11, 11], fill=color) # hotbar rep

def d_screen(draw, color): # toggle_fullscreen
    draw.rectangle([3, 4, 12, 11], outline=color)
    draw.line([1, 1, 4, 1], fill=color)
    draw.line([1, 1, 1, 4], fill=color)
    draw.line([14, 14, 11, 14], fill=color)
    draw.line([14, 14, 14, 11], fill=color)

def d_eye(draw, color): # stream
    draw.ellipse([2, 5, 13, 10], outline=color)
    draw.ellipse([6, 6, 9, 9], fill=color)

def d_slash(draw, color): # disabled overlay
    draw.line([2, 13, 13, 2], fill=color, width=2)

# Specific Drawing Wrappers
def d_reload_chunks(draw, color): d_reload(draw, color)
def d_show_hitboxes(draw, color): 
    draw.rectangle([2, 2, 13, 13], outline=color)
    draw.line([2, 2, 13, 13], fill=color)
    draw.line([13, 2, 2, 13], fill=color)
def d_chunk_bounds(draw, color):
    # Grid
    draw.rectangle([1, 1, 14, 14], outline=color)
    draw.line([5, 1, 5, 14], fill=color)
    draw.line([10, 1, 10, 14], fill=color)
    draw.line([1, 5, 14, 5], fill=color)
    draw.line([1, 10, 14, 10], fill=color)
    
def d_shaders(draw, color):
    # Sun icon?
    draw.ellipse([5, 5, 10, 10], outline=color)
    for i in range(0, 16, 4):
        draw.line([8, 2, 8, 4], fill=color) # Ray top
        # ... simplifying for brevity
    draw.line([8, 1, 8, 3], fill=color)
    draw.line([8, 12, 8, 14], fill=color)
    draw.line([1, 8, 3, 8], fill=color)
    draw.line([12, 8, 14, 8], fill=color)

# Define Mapping
icons = {
    "cycle_camera": d_camera,
    "debug_screen": d_debug,
    "disable_shaders": d_shaders,
    "f3_advanced_tooltips": d_tooltips,
    "f3_clear_chat": d_chat,
    "f3_copy_data": d_copy,
    "f3_copy_location": d_copy,
    "f3_cycle_gamemode": d_game_mode,
    "f3_cycle_render_distance": d_view_distance,
    "f3_help": d_tooltips,
    "f3_pause_without_pause_menu": d_pause,
    "f3_reload_chunks": d_reload_chunks,
    "f3_reload_resource_packs": d_reload_chunks,
    "f3_show_chunk_boundaries": d_chunk_bounds,
    "f3_show_hitboxes": d_show_hitboxes,
    "f3_toggle_auto_pause": d_pause,
    "pause_stream": d_pause,
    "screenshot": d_camera,
    "stream_on_of": d_eye,
    "toggle_fullscreen": d_screen,
    "toggle_hud": d_hud
}

# Generate
if not os.path.exists(OUTPUT_DIR):
    os.makedirs(OUTPUT_DIR)

for name, func in icons.items():
    create_image(name, func, disabled=False)
    # Check if disabled exists in original list effectively by just creating it for all that typically support it
    # Ideally checking against the Enum, but generating generally is safe.
    create_image(name, func, disabled=True)

print("Icons generated successfully.")
