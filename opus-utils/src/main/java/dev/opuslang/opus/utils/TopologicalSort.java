package dev.opuslang.opus.utils;

import java.util.*;

public class TopologicalSort<T> {
    private final Map<T, Set<T>> graph;

    public TopologicalSort() {
        this.graph = new HashMap<>();
    }

    public TopologicalSort(int initialCapacity){
        this.graph = new HashMap<>(initialCapacity);
    }

    public TopologicalSort<T> addVertex(T vertex, Set<T> adjacencyList) {
        if(graph.containsKey(vertex)) throw new IllegalArgumentException("Such a key already exists.");
        graph.put(vertex, adjacencyList);
        return this;
    }

    public List<T> sort() {
        Set<T> visited = new HashSet<>();
        Set<T> recursionStack = new HashSet<>();
        List<T> result = new ArrayList<>();

        for (T vertex : graph.keySet()) {
            if (dfs(vertex, visited, recursionStack, result)) {
                throw new IllegalStateException("Graph contains a cycle");
            }
        }

        return result;
    }

    private boolean dfs(T vertex, Set<T> visited, Set<T> recursionStack, List<T> result) {
        if (recursionStack.contains(vertex)) return true; // cycle
        if (visited.contains(vertex)) return false;

        visited.add(vertex);
        recursionStack.add(vertex);
        Set<T> neighbors = graph.get(vertex);
        if(neighbors == null) throw new IllegalStateException("Vertex does not exist.");
        for (T neighbor : neighbors) {
            if (dfs(neighbor, visited, recursionStack, result)) {
                return true; // cycle
            }
        }

        recursionStack.remove(vertex);
        result.add(vertex);
        return false;
    }
}
