package old;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

class StaticQueue implements Queue<Node> {
	int first;
	int last;
	Node[] list;

	public StaticQueue(int size) {
		// TODO Auto-generated constructor stub
		list = new Node[size];
	}

	@Override
	public boolean addAll(Collection<? extends Node> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		first = -1;
		last = -1;
	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return last == -1;
	}

	@Override
	public Iterator<Node> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Node arg0) {
		list[++last] = arg0;
		return true;
	}

	@Override
	public Node element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(Node arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node peek() {
		// TODO Auto-generated method stub
		if (first >= 0) {
			return list[first];
		}
		return null;
	}

	@Override
	public Node poll() {
		Node node = null;
		if (first < last) {
			node = list[++first];
		}
		if (first == last)
			clear();
		return node;
	}

	@Override
	public Node remove() {
		// TODO Auto-generated method stub
		return null;
	}

}