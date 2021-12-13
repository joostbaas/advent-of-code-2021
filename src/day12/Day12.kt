package day12

import doRun
import readInput

typealias Path = List<Node>

class Node(val name: String, var connections: Set<Node>) {
    val isBig = name.first().isUpperCase()

    fun findPathsTo(endNode: Node, alreadyVisited: List<Node>, canVisit: (Node, List<Node>) -> Boolean): List<Path> =
        connections.map { connection ->
            if (connection == endNode) {
                listOf(listOf(this, endNode))
            } else {
                if (canVisit(connection, alreadyVisited)) {
                    val pathsFromConnection = connection.findPathsTo(endNode, alreadyVisited + this, canVisit)
                    pathsFromConnection.map { listOf(this) + it }
                } else {
                    emptyList()
                }
            }
        }.flatten()

    override fun toString(): String {
        return name
    }
}

fun main() {

    fun parseNodes(input: List<String>): Set<Node> {
        val alreadyCreatedNodes = mutableMapOf<String, Node>()
        fun getOrCreate(name: String): Node =
            alreadyCreatedNodes[name]
                ?: Node(name, emptySet()).let {
                    alreadyCreatedNodes.put(it.name, it)
                    it
                }

        val nodes = input.map {
            val (nodeName1: String, nodeName2: String) = it.split('-')
            val node1 = getOrCreate(nodeName1)
            val node2 = getOrCreate(nodeName2)
            node1.connections = node1.connections + setOf(node2)
            node2.connections = node2.connections + setOf(node1)
            setOf(node1, node2)
        }.flatten().toSet()
        return nodes
    }

    fun part1(input: List<String>): Int {
        val nodes = parseNodes(input)
        val startNode = nodes.find { it.name == "start" }!!
        val endNode = nodes.find { it.name == "end" }!!
        val canVisit: (Node, List<Node>) -> Boolean = { node, alreadyVisited -> node.isBig || node !in alreadyVisited }
        return startNode.findPathsTo(endNode, emptyList(), canVisit).size
    }

    fun part2(input: List<String>): Int {
        val nodes = parseNodes(input)
        val startNode = nodes.find { it.name == "start" }!!
        val endNode = nodes.find { it.name == "end" }!!
        nodes.filter {
            when {
                it.isBig -> false
                it == startNode -> false
                it == endNode -> false
                else -> true
            }
        }.map {
            smallNodeThatCanBeVisitedTwice ->
            val canVisit: (Node, List<Node>) -> Boolean = { node, alreadyVisited ->
                when {
                    node.isBig -> true
                    node !in alreadyVisited -> true
                    node == smallNodeThatCanBeVisitedTwice ->
                        alreadyVisited.count { it == node } < 2
                    else -> false
                }
            }
            startNode.findPathsTo(endNode, emptyList(), canVisit)
        }.flatten().toSet().let {
            return it.size
        }
    }

    val testInput = readInput("day12/testInput")
    val input = readInput("day12/input")

    doRun("part1 test", 10) { part1(testInput) }
    doRun("part1 real", 3230) { part1(input) }
    doRun("part2 test", 36) { part2(testInput) }
    doRun("part2 real", 83475) { part2(input) }
}
