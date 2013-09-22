package org.keyboardplaying.tree.comparer;

import org.keyboardplaying.tree.model.Node;
import org.keyboardplaying.tree.model.Tree;
import org.keyboardplaying.tree.model.Versions;

public class Comparer {

	public <R extends Comparable<R>, T extends Comparable<T>> Tree<Versions<R>, Versions<T>> compare(
			Tree<R, T>... trees) {
		int nbVersions = trees.length;

		if (nbVersions < 2) {
			throw new IllegalArgumentException(
					"The comparator works if there are at least two trees to compare.");
		}

		Versions<R> roots = new Versions<R>(nbVersions);
		@SuppressWarnings("unchecked")
		Node<T>[] children = new Node[nbVersions];

		for (int i = 0; i < nbVersions; i++) {
			roots.setVersion(i, trees[i].getRootInfo());
			children[i] = trees[i].getRoot();
		}

		// FIXME compare

		return null;
	}

}
