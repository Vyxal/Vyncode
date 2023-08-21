# Welcome to Vyncode

Vyncode, pronounced "vine code" for consistency with the rest of the organisation, is a compression system for Vyxal programs. It turns SBCS code into a string of bits, which may or may not result in a whole amount of bytes. Often times, you'll see some fraction of a byte in the byte count.

Vyncode uses a [range coder](https://en.wikipedia.org/wiki/Range_coding) that has been trained on 1400+ vyxal answers from various sources. A weighted positions algorithm is used.

This document will serve as a Q&A guide for any questions you have about how Vyncode works, or how it's used in code golf answers. It will be updated as needed if people have commonly asked questions or more information emerges about the system. 

## Q: How does Vyncode Work With the Code Golf StackExchange?

Vyncode works by taking a program written in SBCS and converting it to a string of bits via a range coding compression method. This bitstring is what determines the score for code golf answers. The answer template generator converts bitstrings to sbcs form for answers for the sake of people reading the answer on the site. Because a long string of 50-200 bits doesn't really give an idea of what the program is actually doing.

So technically, you're submitting the bitstring, but it's been formatted for brevity. The ! flag makes that more explicit, while the = makes that more implicit.

## Q: How do I use Vyncode?

Firstly, you can use sbt to run Vyncode from scala:

```
sbt vyncodeJVM/run -m <method> -v <version> -p <program>
```

`method` is one of `encode` or `decode` (`encode` is used by default).

`version` is the version of Vyncode to use. This defaults to the latest version and is not a necessary parameter. 

`program` is the program/bitstring to encode/decode respectively. 

Secondly, you can compile the js version for usage in web pages:

```
sbt fastOptJS
```

An api guide on how to use the js version will be provided in the near future. 

Thirdly, you can build a jar and call it in the same way you would with sbt minus the sbt part:

```
sbt assembly 
# once you have the jar file
java -jar vyncode.jar <arguments>
```

## Q: What's that number next to the word "bits" in the answer header?

That number represents the version of Vyncode used to score the answer. As the corpus and prediction function may change over time, what was scored as x bits at the time of posting might be scored as y bits in the future. Hence, the version number tells you what version was used to score. It's also loaded into the website via the permalink generated.

## Q: You can't have a bit count that's not a multiple of 4 or 8 as a byte count! You can't possibly store that as a file.

Two things:

a) [the code golf community decided it's allowed](https://codegolf.meta.stackexchange.com/a/24296/78850)
b) programs can't be padded at all, as that would change the meaning of the program.

## Q: Shouldn't the bitstring be posted in answers? After all, that's the thing that's x amount of bits.

There's nothing stopping you from including the bitstring if that's what you want to do. But for convenience of everyone reading the answer, the sbcs version is displayed.
