package arrow.generic

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.some
import arrow.generic.coproduct2.Coproduct2
import arrow.generic.coproduct2.cop
import arrow.generic.coproduct2.coproductOf
import arrow.generic.coproduct2.fold
import arrow.generic.coproduct2.map
import arrow.generic.coproduct2.select
import arrow.generic.coproduct22.Coproduct22
import arrow.generic.coproduct3.Third
import arrow.generic.coproduct3.cop
import arrow.generic.coproduct3.fold
import arrow.generic.coproduct3.map
import arrow.generic.coproduct3.select
import arrow.test.UnitSpec
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(KTestJUnitRunner::class)
class CoproductTest : UnitSpec() {
    init {
        "Coproducts should be generated up to 22" {
            var two: Coproduct2<Unit, Unit>? = null
            var twentytwo: Coproduct22<Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit>? = null
        }

        "select should return None if value isn't correct type" {
            val coproduct2 = "String".cop<String, Long>()

            coproduct2.select<Long>() shouldBe None
        }

        "select returns Some if value is correct type" {
            val coproduct2 = "String".cop<String, Long>()

            coproduct2.select<String>() shouldBe Some("String")
        }

        "coproductOf(A) should equal cop<A, B>()" {
            "String".cop<String, Long>() shouldBe coproductOf<String, Long>("String")
        }

        "Coproduct2 should map over all types but only operate on the present instance" {
            val coproduct2 = "String".cop<Long, String>()

            coproduct2.map(
                    { 6L },
                    { BigDecimal.ONE }
            ) shouldBe BigDecimal.ONE.cop<Long, BigDecimal>()
        }

        "Coproduct2 fold" {
            val coproduct2 = 100L.cop<Long, Int>()

            coproduct2.fold(
                    { "Long$it" },
                    { "Int$it" }
            ) shouldBe "Long100"
        }

        "Coproduct3 should handle multiple nullable types" {
            val value: String? = null
            val coproduct3 = value.cop<Long, Float?, String?>()

            coproduct3.select<Long>() shouldBe None
            coproduct3.select<Float?>() shouldBe None
            coproduct3.select<String?>() shouldBe None //This is weird but since we're using Option it makes sense?

            coproduct3.fold(
                    { "First" },
                    { "Second" },
                    { "Third" }
            ) shouldBe "Third"

            coproduct3.map(
                    { "First" },
                    { "Second" },
                    { "Third" }
            ) shouldBe Third<Long, Float?, String?>("Third")
        }

        "Coproduct3 should handle multiple types with generics" {
            val value: Option<String> = "String".some()
            val coproduct3 = value.cop<Option<Long>, Option<Float>, Option<String>>()

            coproduct3.select<Option<Long>>() shouldBe None
            coproduct3.select<Option<Float>>() shouldBe None
            coproduct3.select<Option<String>>() shouldBe value.some()

            coproduct3.fold(
                    { "First" },
                    { "Second" },
                    { "Third" }
            ) shouldBe "Third"

            coproduct3.map(
                    { "First" },
                    { "Second" },
                    { "Third" }
            ) shouldBe Third<Long, Float?, String?>("Third")
        }
    }
}