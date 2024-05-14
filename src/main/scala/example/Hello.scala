package example

import java.time.{Duration, Instant}
import scala.util.Random

object Hello extends App {

  val iter = 8_000_000
//  val loops = 32_000_000
  val loops = 20

  def f1() = {
    val tst1 = Instant.now()
    1.to(iter).map { s =>
      s"aaaaaaaaa " +
        s"aaaaaaaaa" + s
    }.foreach(_ => "")

    val lapse = Duration.between(tst1, Instant.now())
    val name = "s+s+s______"
    println(s"$name: $lapse")
    name -> lapse
  }

  def f1m() = {
    val tst1 = Instant.now()
    1.to(iter).map { s =>
      s"aaaaaaaaa " +
        s"aaaaaaaaa" + s
    }.foreach(_ => "")

    val lapse = Duration.between(tst1, Instant.now())
    val name = "s+s+s+s+s+s"
    println(s"$name: $lapse")
    name -> lapse
  }

  def f2() = {
    val tst1 = Instant.now()
    1.to(iter).map { s =>
      s"aaaaaaaaa aaaaaaaaa $s"
    }.foreach(_ => "")

    val lapse = Duration.between(tst1, Instant.now())
    val name = "stringBuild"
    println(s"$name: $lapse")
    name -> lapse
  }

  def f3() = {
    val tst1 = Instant.now()
    1.to(iter).map { s =>
      s"""aaaaaaaaa
         | $s""".stripMargin
    }.foreach(_ => "")

    val lapse = Duration.between(tst1, Instant.now())
    val name = "stripMargin"
    println(s"$name: $lapse")
    name -> lapse
  }

  private val calls: Seq[() => (String, Duration)] = Seq(f1, f1m, f2, f3)

  val res = 1.to(loops)
    .flatMap { _ =>
      calls.map(f => f())
    }
  //println(res)

  val res2 = res.groupMapReduce{ case(k, _) => k }(e => e._2)((d1, d2) => d1.plus(d2))
  val allIterations = 1L * iter * loops
  println(s"Mixed iterations: $allIterations")
  println(res2)
  val times = res2.map(_._2)
  val tmax = times.max
  val tmin = times.min
  //val diff = Duration.between(tmin, tmax)
  println(s"Diff: ${tmax.minus(tmin)}")

  res2.foreach { case(n, duration) =>
    println(s"$n: Diff to fastest: ${duration.minus(tmin)}")
    println(s"$n: Diff/1 to fastest: ${duration.minus(tmin).toNanos.toDouble / allIterations} nanos")
  }

  //Random.shuffle(calls).foreach(f => f)
}
