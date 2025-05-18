extends Node2D

# 网格容器
@onready var level_container = $LevelContainer
# 当前选中的关卡
var selected_level: String = ""
func _ready():
	load_levels()

func load_levels():
	# 清空现有按钮
	for child in level_container.get_children():
		child.queue_free()
	
	# 获取map目录下的文件
	var dir = DirAccess.open("map")
	if dir:
		var files = []
		dir.list_dir_begin()
		var file_name = dir.get_next()
		while file_name != "":
			if not dir.current_is_dir() and file_name.get_extension() == "txt":
				files.append(file_name.get_basename())
			file_name = dir.get_next()
		
		# 按数字排序
		files.sort_custom(func(a, b): return a.to_int() < b.to_int())
		
		# 创建按钮
		var theme = load("res://custom_theme.tres")
		for level in files:
			var button = Button.new()
			button.text = "关卡 %s" % level
			button.pressed.connect(_on_level_button_pressed.bind(level))
			button.theme = theme
			level_container.add_child(button)

func _on_level_button_pressed(level_name: String):
	selected_level = level_name
	# 跳转到游戏场景
	var main_scene = preload("res://scenes/Main.tscn").instantiate()
	# 传递参数
	main_scene.selected_level = selected_level
	get_tree().root.add_child(main_scene)
	queue_free()
