extends ColorRect
class_name ColorBlock

signal block_clicked(pos)

var grid_position = Vector2.ZERO

func _ready():
	mouse_filter = Control.MOUSE_FILTER_STOP
	gui_input.connect(_on_gui_input)

func _on_gui_input(event):
	if event is InputEventMouseButton and event.pressed and event.button_index == MOUSE_BUTTON_LEFT:
		block_clicked.emit(grid_position)
