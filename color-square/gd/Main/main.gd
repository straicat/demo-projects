extends Node2D

@onready var step_label = $Step
@onready var target_label = $Target
@onready var board = $ColorBoard/Board

var selected_level = "1"

func _ready():
	board.connect("step_update", _on_step_updated)
	board.connect("init_target", _on_init_target)
	var b = get_node("ColorBoard/Board")
	b.selected_level = selected_level

func _on_step_updated(step):
	step_label.text = "[font_size=24]还剩 [color=red][b]%d[/b][/color] 步[/font_size]" % step

func _on_init_target(color):
	var target = "ERROR"
	if color == 1:
		target = "[color=blue]【蓝色】[/color]"
	elif color == 2:
		target = "[color=red]【红色】[/color]"
	elif color == 3:
		target = "[color=yellow]【黄色】[/color]"
	elif color == 4:
		target = "[color=green]【绿色】[/color]"
	target_label.text = "[font_size=20]将色块全部染成%s[/font_size]" % target


func _on_back_pressed() -> void:
	get_tree().change_scene_to_file("res://scenes/LevelSelect.tscn")
	queue_free()
