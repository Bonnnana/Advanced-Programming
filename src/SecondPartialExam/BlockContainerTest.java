// 16.

package SecondPartialExam;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>{

	List<TreeSet<T>> blocks;
	int blockSize;

	public BlockContainer(int blockSize) {
		this.blockSize = blockSize;
		blocks = new ArrayList<>();
	}
	public void add(T a){
		if(blocks.isEmpty()|| (blocks.get(blocks.size()-1).size()>= blockSize)) {
			TreeSet<T> newBlock = new TreeSet<>();
			blocks.add(newBlock);
		}
		blocks.get(blocks.size()-1).add(a);
	}

	public boolean remove(T a){
		boolean removed = false;
		if(!blocks.get(blocks.size()-1).isEmpty()){
			removed = blocks.get(blocks.size()-1).remove(a);
			if(blocks.get(blocks.size()-1).isEmpty())
				blocks.remove(blocks.size()-1);
		}
		return removed;
	}

	public void sort(){
		List<T> sorted= blocks.stream()
				.flatMap(el -> el.stream())
				.sorted()
				.collect(Collectors.toList());

		blocks.clear();
		sorted.forEach(el-> add(el));
	}

	@Override
	public String toString() {
		return blocks.stream()
				.map(block -> block.stream()
						.map(el->el.toString())
						.collect(Collectors.joining(", ", "[", "]")))
				.collect(Collectors.joining(","));
	}
}
public class BlockContainerTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int size = scanner.nextInt();
		BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
		scanner.nextLine();
		Integer lastInteger = null;
		for(int i = 0; i < n; ++i) {
			int element = scanner.nextInt();
			lastInteger = element;
			integerBC.add(element);
		}
		System.out.println("+++++ Integer Block Container +++++");
		System.out.println(integerBC);
		System.out.println("+++++ Removing element +++++");
		integerBC.remove(lastInteger);
		System.out.println("+++++ Sorting container +++++");
		integerBC.sort();
		System.out.println(integerBC);
		BlockContainer<String> stringBC = new BlockContainer<String>(size);
		String lastString = null;
		for(int i = 0; i < n; ++i) {
			String element = scanner.next();
			lastString = element;
			stringBC.add(element);
		}
		System.out.println("+++++ String Block Container +++++");
		System.out.println(stringBC);
		System.out.println("+++++ Removing element +++++");
		stringBC.remove(lastString);
		System.out.println("+++++ Sorting container +++++");
		stringBC.sort();
		System.out.println(stringBC);
	}
}




