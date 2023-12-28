class Node:
    def __init__(self):
        self.data = None
        self.left = None
        self.right = None

    def setLeft(self, left):
        self.left = left

    def setRight(self, right):
        self.right = right

    def setData(self, data):
        self.data = data

    def __str__(self):
        return str(self.data)
    
    def isEmpty(self):
        return self.data == None
    
    def __iter__(self):
        self.stack = [] 
        self.travel(self)
        return iter(self.stack)

    def travel(self, node):
        if(node.left):
            self.travel(node.left)

        self.stack.append(node.data)

        if(node.right):
            self.travel(node.right) 

    
class BinarySearchTree:
    def __init__(self, name:str, root:Node):
        self.root = root
        self.name = name
    
    def add(self, element):
        if(self.root.isEmpty()):
            self.root.setData(element)
        else:
            self.addHelp(self.root,element)


    def addHelp(self, node, element):
        if(element > node.data):
            if node.right is None:
                newnode = Node()
                newnode.setData(element)
                node.setRight(newnode)
            else:
                self.addHelp(node.right, element)

        elif(element < node.data):
            if node.left is None:
                newnode = Node()
                newnode.setData(element)
                node.setLeft(newnode)
            else:
                self.addHelp(node.left, element)

    def add_all(self, *val):
        for i in range(len(val)):
            self.add(val[i])

    def __str__(self) -> str:
        display = "[" + self.name + "]"
        display += " " + self.showHelp(self.root,"")
        return display

    def showHelp(self, node, display):
        display += node.__str__()

        if(node.left):
            display += " L:(" + self.showHelp(node.left, "") + ")"

        if(node.right):
            display += " R:(" + self.showHelp(node.right, "") + ")"

        return display
            

if __name__ == "__main__":
    tree = BinarySearchTree(name="Oak", root=Node())
    # the next line adds the elements in the order 5, 3, 9, and then 0 tree.add_all(5, 3, 9, 0)
    tree.add_all(5,7,9,0)
    print(tree)
    t1 = BinarySearchTree(name="Oak", root=Node()) 
    t1.add_all(1, 0, 10, 2, 7)
    for x in t1.root:
        print(x)


