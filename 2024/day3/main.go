package main

import (
	"fmt"
	"io"
	"os"
	"regexp"
	"strconv"
)

func main() {
	part1()
	part2()
}

func part1() {
	input := readInput()
	r := regexp.MustCompile(`mul\((\d+),(\d+)\)`)

	allMatches := r.FindAllStringSubmatch(input, -1)

	sum := 0
	for _, match := range allMatches {
		m1, err := strconv.Atoi(match[1])
		if err != nil {
			panic(err)
		}
		m2, err := strconv.Atoi(match[2])
		if err != nil {
			panic(err)
		}
		sum += m1 * m2
	}
	fmt.Println("Part 1 -", sum)
}

func part2() {
	input := readInput()

	re := regexp.MustCompile(`don't\(\).*?do\(\)`)
	input = re.ReplaceAllString(input, "")

	r := regexp.MustCompile(`mul\((\d+),(\d+)\)`)

	allMatches := r.FindAllStringSubmatch(input, -1)

	sum := 0
	for _, match := range allMatches {
		m1, err := strconv.Atoi(match[1])
		if err != nil {
			panic(err)
		}
		m2, err := strconv.Atoi(match[2])
		if err != nil {
			panic(err)
		}
		sum += m1 * m2
	}
	fmt.Println("Part 2 -", sum)
}

func readInput() string {
	f, err := os.Open("day3/input.txt")
	if err != nil {
		panic(err)
	}
	defer f.Close()

	input, err := io.ReadAll(f)
	if err != nil {
		panic(err)
	}
	return string(input)
}
