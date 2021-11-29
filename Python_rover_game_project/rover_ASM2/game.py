import os
from loader import *


def quit():
	"""
	Will quit the program
	"""
	exit()


def menu_help():
	"""
	Displays the help menu of the game
	"""
	msg = """
START <level file> - Starts the game with a provided file.
QUIT - Quits the game
HELP - Shows this message
"""
	print(msg)


def menu_start_game(filepath):
	"""
	Will start the game with the given file path
	"""
	if not os.path.exists(filepath):
		print()
		print("Level file could not be found")
		print()
	else:
		result, pt, rov = load_level(filepath)
		if not result:
			print()
			print("Unable to load level file")
			print()
		else:
			# print(pt.tiles[1][2].type)
			# print(rov.elv)
			while True:
				action = input()
				# print(action)
				if action == "FINISH":
					print("""
You explored {0}% of {1}
""".format(int(pt.ratio * 100 / (pt.width * pt.height)), pt.name))
					break
				if action == "STATS":
					print( )
					print("Explored: {0}%".format(int(pt.ratio * 100 / (pt.width * pt.height))))
					print("Battery: {0}/100".format(rov.battery))
					print( )
					continue
				if "MOVE" in action:
					try:
						direct = action.strip().split(" ")[1]
						cycles = int(action.strip().split(" ")[2])
						if not direct == "N" and not direct == "E" and not direct == "S" and not direct == "W":
							continue
						if cycles <= 0:
							continue
						rov = pt.explore(direct, cycles, rov)
						#print("New pos: {0} {1}, elevation: {2}, battery: {3}".format(rov.x, rov.y, rov.elv, rov.battery))
					except:
						pass
				if "SCAN" in action:
					try:
						mode = action.strip().split(" ")[1]
						if mode == "shade":
							pt.scan_shade(rov.x, rov.y)
						elif mode == "elevation":
							pt.scan_elevation(rov.x, rov.y, rov.elv)
						else:
							print()
							print("Cannot perform this command")
							print()
						for i in range(-2,3):
							for j in range(-2,3):
								tmp_row = pt.cycle(rov.x + i, pt.height)
								tmp_col = pt.cycle(rov.y + j, pt.width)
								if not pt.tiles[tmp_row][tmp_col].explored:
									pt.tiles[tmp_row][tmp_col].explored = True
									pt.ratio += 1
					except:
						pass
				if "WAIT" in action:
					try:
						cycles = action.strip().split(" ")[1]
						if not pt.tiles[rov.x][rov.y].is_shaded():
							rov.wait(cycles)
					except:
						pass


def menu():
	"""
	Start the menu component of the game
	"""
	select = input()
	if "START" in select:
		try:
			filepath = select.split(' ')[1]
			menu_start_game(filepath)
			return
		except:
			print()
			print("Unable to load level file")
			print()
	elif select == "HELP":
		menu_help()
	elif select == "QUIT":
		quit()
	else:
		print()
		print("No menu item")
		print()		

if __name__ == '__main__':
	menu()
