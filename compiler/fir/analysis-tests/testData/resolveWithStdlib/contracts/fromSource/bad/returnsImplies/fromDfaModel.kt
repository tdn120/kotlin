import kotlin.contracts.*

interface MyInterface

class MyGoodClass : MyInterface
class MyBadClass : MyInterface

@OptIn(ExperimentalContracts::class)
fun MyInterface.isGood(): Boolean {
    <!WRONG_IMPLIES_CONDITION!>contract {
        returns(true) implies (this@isGood is MyGoodClass)
        returns(false) implies (this@isGood is MyBadClass)
    }<!>
    return this@isGood is MyGoodClass
}