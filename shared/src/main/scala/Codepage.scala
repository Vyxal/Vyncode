package vyncode

object Codepage:
  val codepage = """|λƛ¬∧⟑∨⟇÷×«\n»°•ß†€
                    |½∆ø↔¢⌐æʀʁɾɽÞƈ∞¨ 
                    |!\"#$%&'()*+,-./01
                    |23456789:;<=>?@A
                    |BCDEFGHIJKLMNOPQ
                    |RSTUVWXYZ[\\]`^_abc
                    |defghijklmnopqrs
                    |tuvwxyz{|}~↑↓∴∵›
                    |‹∷¤ð→←βτȧḃċḋėḟġḣ
                    |ḭŀṁṅȯṗṙṡṫẇẋẏż√⟨⟩
                    |‛₀₁₂₃₄₅₆₇₈¶⁋§ε¡
                    |∑¦≈µȦḂĊḊĖḞĠḢİĿṀṄ
                    |ȮṖṘṠṪẆẊẎŻ₌₍⁰¹²∇⌈
                    |⌊¯±₴…□↳↲⋏⋎꘍ꜝ℅≤≥
                    |≠⁼ƒɖ∪∩⊍£¥⇧⇩ǍǎǏǐǑ
                    |ǒǓǔ⁽‡≬⁺↵⅛¼¾Π„‟""".stripMargin

  def vyxalToInt(s: String): Seq[Int] = s.map(codepage.indexOf(_))

  def intToVyxal(lst: Seq[Int]): String = lst.map(codepage(_)).mkString("")
end Codepage
