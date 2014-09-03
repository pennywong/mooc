"""
Degree distributions for graphs
"""

EX_GRAPH0 = {0:set([1,2]), 1:set(), 2:set()}
EX_GRAPH1 = {0:set([1,4,5]), 1:set([2,6]), 2:set([3]), 3:set([0]), 4:set([1]),
			5:set([2]),6:set()}
EX_GRAPH2 = {0:set([1,4,5]), 1:set([2,6]), 2:set([3,7]), 3:set([7]), 4:set([1]),
			5:set([2]),6:set(),7:set([3]),8:set([1,2]),9:set([0,3,4,5,6,7])}

def make_complete_graph(num_nodes):
	"""
	Complete directed graph with the specified number of nodes
	"""
	graph = {}
	for row in range(num_nodes):
		edges = set()
		for col in range(num_nodes):
			if row!=col:
				edges.add(col)
		graph[row] = edges
	return graph

def compute_in_degrees(digraph):
	"""
	Computes the in-degrees for the nodes in the graph
	"""
	result = {}
	for key in digraph:
		result[key] = 0
		
	for key in digraph:
		edges = digraph[key]
		for edge in edges:
			result[edge] += 1
			
	return result
	
def in_degree_distribution(digraph):
	"""
	Computes the unnormalized distribution of the in-degrees of the graph
	"""
	in_degrees = compute_in_degrees(digraph)
	distribution = {}
	for key in in_degrees:
		value = in_degrees[key]
		if distribution.has_key(value):
			distribution[value] += 1
		else:
			distribution[value] = 1
	return distribution