
class Tile:

	def __init__(self, type, *args):
		"""
		Initialises the terrain tile and attributes
		"""
		self.type = type
		self.elv = []
		for arg in args:
			self.elv.append(int(arg))
		self.occupant = None
		self.explored = False

	def elevation(self):
		"""
		Returns an integer value of the elevation number
		of the terrain object
		"""
		return self.elv

	def is_shaded(self):
		"""
		Returns True if the terrain tile is shaded, otherwise False
		"""
		if self.type == "shaded":
			return True
		return False
#see if it is up/down slope or shaded or unshaded?
	def set_occupant(self, obj):
		"""
		Sets the occupant on the terrain tile
		"""
		self.occupant = obj

	def get_occupant(self):
		"""
		Gets the entity on the terrain tile
		If nothing is on this tile, it should return None
		"""
		return self.occupant
	
	
	
