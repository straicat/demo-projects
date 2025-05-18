extends HFlowContainer

# 预定义颜色
const COLORS = [
	Color.BLUE,  # 蓝
	Color.RED,  # 红
	Color.YELLOW,  # 黄
	Color.GREEN   # 绿
]

const CELL_SIZE = 64

# 引用要设置颜色的Node2D节点
@export var target_node: Node2D

func _ready():
	# 创建颜色按钮
	for color in COLORS:
		var button = ColorRect.new()
		button.color = color
		button.custom_minimum_size = Vector2(CELL_SIZE, CELL_SIZE)  # 设置按钮大小
		
		# 添加点击检测区域
		var ctrl = Control.new()
		ctrl.custom_minimum_size  = button.custom_minimum_size
		ctrl.mouse_filter = Control.MOUSE_FILTER_PASS
		ctrl.gui_input.connect(_on_color_button_input.bind(color))
		button.add_child(ctrl)
		add_child(button)

func _on_color_button_input(event: InputEvent, color: Color):
	if event is InputEventMouseButton and event.pressed and event.button_index == MOUSE_BUTTON_LEFT:
		if target_node and target_node.has_method("set_selected_color"):
			target_node.set_selected_color(color)
