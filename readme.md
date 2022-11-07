# dice

An example scala-native program that can roll and analyse sets of dice rolls.

## Building

```bash
nix-shell -p clang
sbt
project diceNative
nativeLink
```

Will build a binary at `./dice/native/target/scala-3.1.1/dice-out`

## Usage

```
dice
Usage:
    dice roll
    dice analyse
    dice repl

a utility for dice

Options and flags:
    --help
        Display this help text.

Subcommands:
    roll
        Roll some dice
    analyse
        Analyse some dice
    repl
        Start the dice REPL
```
