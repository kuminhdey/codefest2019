package old;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class StaticArrayMoveList implements List<SpecialMove>{
	SpecialMove[] list;
	int index = -1;
	
	public StaticArrayMoveList(int size){
		list = new SpecialMove[size];
	}

	@Override
	public boolean add(SpecialMove arg0) {
		list[++index] = arg0;
		return true;
	}

	@Override
	public void add(int arg0, SpecialMove arg1) {
		// TODO Auto-generated method stub
		list[arg0] = arg1;
	}

	@Override
	public boolean addAll(Collection<? extends SpecialMove> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends SpecialMove> arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		index = -1;
		
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
	public SpecialMove get(int arg0) {
		if(index != -1){
			return list[arg0];
		}
		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return index == -1;
	}

	@Override
	public Iterator<SpecialMove> iterator() {
		return new Iterator<SpecialMove>() {
			int itIndex = 0;
			@Override
			public SpecialMove next() {
				if(hasNext()){
					return list[itIndex++];
				}
				return null;
			}
			
			@Override
			public boolean hasNext() {
				return itIndex<= index;
			}
		};
	}

	@Override
	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<SpecialMove> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<SpecialMove> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		return false;
	}

	@Override
	public SpecialMove remove(int arg0) {
		// TODO Auto-generated method stub
		SpecialMove a = list[arg0];
		list[arg0] = null;
		return a;
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
	public SpecialMove set(int arg0, SpecialMove arg1) {
		 list[arg0] = arg1;
		return arg1;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return index+1;
	}

	@Override
	public List<SpecialMove> subList(int arg0, int arg1) {
		return null;
	}

	@Override
	public Object[] toArray() {
		SpecialMove[] nodes = null;
		if (index >=0){
			nodes = new SpecialMove[index+1];
			for (int i = 0; i <= index ; i++) {
				nodes[i]= list[i];
			}
		}
		return nodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return (T[]) list;
	}
	

}
