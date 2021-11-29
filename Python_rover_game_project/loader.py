from planet import Planet
from rover import *



def load_level(filename):
	"""
	Loads the level and returns an object of your choosing
	"""
	result = True
	pt = Planet("xx", 0, 0)
	rov = Rover(0, 0, 0)
	with open(filename) as fp:
		lines = fp.readlines()
		# print(lines)
		# first line should be "[planet]"
		if not lines[0].strip() == "[planet]":
			return False, None, None

		# extract name
		try:
			if not lines[1].split(",")[0] == "name":
				return False, None, None
			if not lines[2].split(",")[0] == "width":
				return False, None, None
			if not lines[3].split(",")[0] == "height":
				return False, None, None
			if not lines[4].split(",")[0] == "rover":
				return False, None, None

			planet_name = lines[1].strip().split(",")[1]
			width = int(lines[2].strip().split(",")[1])
			height = int(lines[3].strip().split(",")[1])
			rov_y = int(lines[4].strip().split(",")[1])
			rov_x = int(lines[4].strip().split(",")[2])
			if width < 5 or height < 5:#need 5x5 plain size
				return False, None, None

			# if len(lines) != width * height + 7 and len(lines) != width * height + 8: 
			# 	return False, None, None
			
			if width < rov_y or height < rov_x or rov_x < 0 or rov_y < 0: 
				return False, None, None

			pt = Planet(planet_name, width, height)

			for i in range(7, len(lines)):
				if lines[i].strip() == "": continue
				row = int((i - 7) / width)
				col = int((i - 7) % width)
				pt.tiles[row][col].type = lines[i].strip().split(",")[0]
				pt.tiles[row][col].elv = [int(e) for e in lines[i].strip().split(",")[1:]]
			rov = Rover(rov_x, rov_y, pt.tiles[rov_x][rov_y].elv)
			pt.tiles[rov_x][rov_y].explored = True
			pt.ratio += 1
			
		except Exception:
			return False, None, None

	return result, pt, rov
				
				
				
				
				
				
				

				
				
				
				
				
				
				
				
				
