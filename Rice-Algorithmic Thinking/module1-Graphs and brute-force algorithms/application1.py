import random
import urllib2
import matplotlib.pyplot as plt

CITATION_URL = "http://storage.googleapis.com/codeskulptor-alg/alg_phys-cite.txt"
#CITATION_URL = "file:///e|/mooc/alg_phys-cite.txt"

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
	
def compute_out_degrees(digraph):
	"""
	Computes the out-degrees for the nodes in the graph
	"""
	result = {}
	for key in digraph:
		result[key] = len(digraph[key])
			
	return result
	
def compute_nodes_edges(digraph):
	nodes = len(digraph)
	out_degrees = compute_out_degrees(digraph)
	edges = 0
	for key in out_degrees:
		edges += out_degrees[key]
	return nodes, edges
	
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
	
def load_graph(graph_url):
    """
    Function that loads a graph given the URL
    for a text representation of the graph
    
    Returns a dictionary that models a graph
    """
    graph_file = urllib2.urlopen(graph_url)
    graph_text = graph_file.read()
    graph_lines = graph_text.split('\n')
    graph_lines = graph_lines[ : -1]
    
    print "Loaded graph with", len(graph_lines), "nodes"
    
    answer_graph = {}
    for line in graph_lines:
        neighbors = line.split(' ')
        node = int(neighbors[0])
        answer_graph[node] = set([])
        for neighbor in neighbors[1 : -1]:
            answer_graph[node].add(int(neighbor))

    return answer_graph
	
def citation_graph():
	graph = load_graph(CITATION_URL)
	plot_distribution(graph)
	
def plot_distribution(graph):
	distribution = in_degree_distribution(graph)
	total = 0.0
	for key in distribution:
		if distribution[key] != 0:
			total += distribution[key]
	x = []
	y = []
	for key in distribution:
		if key!=0 and distribution[key] != 0:
			x.append(key)
			y.append(distribution[key]/total)
	
	plt.title('In-degree distribution for DPA')
	plt.xlabel('In-degree')
	plt.ylabel('Distribution')
	plt.loglog(x,y,'bo')
	plt.show()
	
def er(num, p):
	graph = {}
	
	for i in range(num):
		graph[i] = set()
	
	for i in range(num):
		for j in range(num):
			if i!=j:
				r = random.random()
				if r < p:
					graph[i].add(j)
					graph[j].add(i)
	return graph
	
def test_er(num, p):
	graph = er(num, p)
	plot_distribution(graph)
	
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

	
class DPATrial:
    def __init__(self, num_nodes):        
        self._num_nodes = num_nodes
        self._node_numbers = [node for node in range(num_nodes) for dummy_idx in range(num_nodes)]

    def run_trial(self, num_nodes):
        # compute the neighbors for the newly-created node
        new_node_neighbors = set()
        for dummy_idx in range(num_nodes):
            new_node_neighbors.add(random.choice(self._node_numbers))
        
        # update the list of node numbers so that each node number 
        # appears in the correct ratio
        self._node_numbers.append(self._num_nodes)
        self._node_numbers.extend(list(new_node_neighbors))
        
        #update the number of nodes
        self._num_nodes += 1
        return new_node_neighbors
	
def DPA(n, m):
	graph = make_complete_graph(m)
	in_degrees = compute_in_degrees(graph)
	trial = DPATrial(m)
	
	for i in range(m, n):		
		edges = trial.run_trial(m)		
		graph[i] = edges
		
		in_degrees[i] = 0
		for k in edges:
			in_degrees[k] += 1
		
	return graph

		