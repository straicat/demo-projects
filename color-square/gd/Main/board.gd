extends Node2D
# 配置参数
const BOARD_WIDTH = 10
const BOARD_HEIGHT = 8
const COLORS = [Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE]
const CELL_SIZE = 64

var selected_level = "1"

var board = []
var remain_step = 0
var selected_color = null
var target_color = 1

signal step_update(step: int)
signal init_target(color: int)

const COLOR_MAP = {
	1: Color.BLUE,
	2: Color.RED,
	3: Color.YELLOW,
	4: Color.GREEN
}

func _ready():
	call_deferred("init_config_show")

func init_config_show():
	board = []
	selected_color = null
	var matrix = read_matrix_from_file("map/%s.txt" % selected_level)
	if matrix:
		generate_color_grid(matrix)
	step_update.emit(remain_step)
	init_target.emit(target_color)

# 读取文件并解析为数字矩阵 (Godot 4.x 使用 FileAccess)
func read_matrix_from_file(file_path):
	if not FileAccess.file_exists(file_path):
		push_error("File not found: " + file_path)
		return null
	
	var file = FileAccess.open(file_path, FileAccess.READ)
	if file == null:
		push_error("Failed to open file: " + file_path + ", Error: " + str(FileAccess.get_open_error()))
		return null
	
	var matrix = []
	
	var lineno = 0
	while not file.eof_reached():
		lineno += 1
		var line = file.get_line()
		if line.strip_edges() == "":
			continue
		if lineno == 1:
			var line_arr = line.split(",", false)
			remain_step = line_arr[0].to_int()
			target_color = line_arr[1].to_int()
			continue
		var row = []
		for num_str in line.split("", false):
			var num = num_str.to_int()
			if num >= 1 and num <= 4:
				row.append(num)
			else:
				row.append(1)
		matrix.append(row)
	
	file.close()
	return matrix

# 生成彩色网格
func generate_color_grid(matrix):
	for row in range(BOARD_HEIGHT):
		var board_row = []
		for col in range(BOARD_WIDTH):
			var cell_value = matrix[row][col]
			var block = ColorBlock.new()
			block.color = COLOR_MAP.get(cell_value, Color.BLUE)
			board_row.append(block.color)
			block.size = Vector2(CELL_SIZE, CELL_SIZE)
			block.position = Vector2(
				col * CELL_SIZE,
				row * CELL_SIZE
			)
			block.grid_position = Vector2(col, row)
			block.block_clicked.connect(_on_block_clicked)
			add_child(block)
		board.append(board_row)

func draw_board():
	# 清除现有的方块
	for child in get_children():
		if child is ColorBlock:
			child.queue_free()
	
	# 绘制新方块
	for y in range(BOARD_HEIGHT):
		for x in range(BOARD_WIDTH):
			var block = ColorBlock.new()
			block.position = Vector2(x * CELL_SIZE, y * CELL_SIZE)
			block.size = Vector2(CELL_SIZE, CELL_SIZE)
			block.color = board[y][x]
			block.grid_position = Vector2(x, y)
			block.block_clicked.connect(_on_block_clicked)
			add_child(block)

func set_selected_color(color: Color):
	selected_color = color

func _on_block_clicked(pos):
	if selected_color != null and board[pos.y][pos.x] != selected_color:
		var connected_blocks = find_connected_blocks(pos.x, pos.y, board[pos.y][pos.x])
		for block_pos in connected_blocks:
			board[block_pos.y][block_pos.x] = selected_color
		draw_board()
		if remain_step > 0:
			remain_step -= 1
		step_update.emit(remain_step)
		check_result()

func check_result():
	var dialog = $AlertDialog
	dialog.title = "结果提示"
	for i in range(BOARD_HEIGHT):
		for j in range(BOARD_WIDTH):
			if board[i][j] != COLOR_MAP[target_color]:
				if remain_step <= 0:
					dialog.dialog_text = "挑战失败，别灰心，请重新尝试~"
					dialog.close_requested.connect(init_config_show)
					dialog.confirmed.connect(init_config_show)
					dialog.popup_centered()
				return

	dialog.dialog_text = "太棒了！挑战成功！"
	dialog.close_requested.connect(_on_success_back)
	dialog.confirmed.connect(_on_success_back)
	dialog.popup_centered()
	return

func _on_success_back():
	get_node("/root/Main").queue_free()
	get_tree().change_scene_to_file("res://scenes/LevelSelect.tscn")

func find_connected_blocks(x, y, color):
	var visited = []
	var to_visit = [[x, y]]
	var connected = []
	
	while to_visit.size() > 0:
		var current = to_visit.pop_back()
		var cx = current[0]
		var cy = current[1]
		
		# 检查是否已经访问过或超出边界
		if [cx, cy] in visited or cx < 0 or cx >= BOARD_WIDTH or cy < 0 or cy >= BOARD_HEIGHT:
			continue
			
		# 检查颜色是否匹配
		if board[cy][cx] != color:
			continue
			
		# 添加到已连接列表
		connected.append(Vector2(cx, cy))
		visited.append([cx, cy])
		
		# 添加相邻方块到待访问列表
		to_visit.append([cx + 1, cy])  # 右
		to_visit.append([cx - 1, cy])  # 左
		to_visit.append([cx, cy + 1])  # 下
		to_visit.append([cx, cy - 1])  # 上
	
	return connected
