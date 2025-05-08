package main

import (
	"bufio"
	"log"
	"math"
	"os"
	"slices"
	"strconv"
	"strings"
)

func main() {
	part1()
	part2()
}

func part1() {
	c1, c2, _, err := readColumns("day1/input.txt")
	if err != nil {
		panic(err)
	}
	slices.Sort(c1)
	slices.Sort(c2)

	acc := 0
	for i := range c1 {
		dist := math.Abs(float64(c1[i]) - float64(c2[i]))
		acc += int(dist)
	}
	log.Println("Part 1 -", acc)
}

func part2() {
	c1, _, count, err := readColumns("day1/input.txt")
	if err != nil {
		panic(err)
	}

	acc := 0
	for i := range c1 {
		num := c1[i]
		acc += num * count[num]
	}
	log.Println("Part 2 -", acc)
}

func readColumns(filename string) ([]int, []int, map[int]int, error) {
	// Open the file
	file, err := os.Open(filename)
	if err != nil {
		return nil, nil, nil, err
	}
	defer file.Close()

	var column1 []int
	var column2 []int
	cnt := make(map[int]int)

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		// Split the line by whitespace
		values := strings.Fields(scanner.Text())
		if len(values) == 2 {
			// Convert strings to integers
			val1, err := strconv.Atoi(values[0])
			if err != nil {
				return nil, nil, nil, err
			}

			val2, err := strconv.Atoi(values[1])
			if err != nil {
				return nil, nil, nil, err
			}

			column1 = append(column1, val1)
			column2 = append(column2, val2)
			cnt[val2]++
		}
	}

	if err := scanner.Err(); err != nil {
		return nil, nil, nil, err
	}

	return column1, column2, cnt, nil
}
