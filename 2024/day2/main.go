package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

func main() {
	part1()
	part2()
}

func part1() {
	reports, err := readNumbersFromFile("day2/input.txt")
	if err != nil {
		panic(err)
	}
	counter := 0
	for _, report := range reports {
		if dist := maxAdjacentDistance(report); isMonotonic(report) && dist >= 1 && dist <= 3 {
			counter++
		}
	}
	fmt.Println("Part 1 -", counter)
}

func part2() {
	reports, err := readNumbersFromFile("day2/input.txt")
	if err != nil {
		panic(err)
	}
	counter := 0
	for _, report := range reports {
		if dist := maxAdjacentDistance(report); isMonotonic(report) && dist >= 1 && dist <= 3 {
			counter++
		} else {
			for i := range report {
				copyReport := make([]int, len(report))
				copy(copyReport, report)
				dampened := append(copyReport[:i], copyReport[i+1:]...)
				if dist := maxAdjacentDistance(dampened); isMonotonic(dampened) && dist >= 1 && dist <= 3 {
					counter++
					break
				}
			}
		}
	}
	fmt.Println("Part 2 -", counter)
}

func maxAdjacentDistance(nums []int) int {
	if len(nums) < 2 {
		return 0
	}

	maxDistance := 0

	for i := 1; i < len(nums); i++ {
		distance := int(math.Abs(float64(nums[i] - nums[i-1])))
		if distance > maxDistance {
			maxDistance = distance
		}
	}

	return maxDistance
}

func isMonotonic(nums []int) bool {
	if len(nums) <= 1 {
		return true
	}

	isIncreasing := true
	isDecreasing := true

	for i := 1; i < len(nums); i++ {
		if nums[i] >= nums[i-1] {
			isDecreasing = false
		}
		if nums[i] <= nums[i-1] {
			isIncreasing = false
		}
	}

	return isIncreasing || isDecreasing
}

func readNumbersFromFile(fileName string) ([][]int, error) {
	file, err := os.Open(fileName)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var numbers [][]int
	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		line := scanner.Text()
		strNumbers := strings.Fields(line)
		var intNumbers []int
		for _, strNum := range strNumbers {
			num, err := strconv.Atoi(strNum)
			if err != nil {
				return nil, err
			}
			intNumbers = append(intNumbers, num)
		}
		numbers = append(numbers, intNumbers)
	}

	if err := scanner.Err(); err != nil {
		return nil, err
	}

	return numbers, nil
}
