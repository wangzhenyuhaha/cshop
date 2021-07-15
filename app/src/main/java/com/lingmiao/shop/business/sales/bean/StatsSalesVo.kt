
import com.google.gson.annotations.SerializedName

data class StatsSalesVo(
    @SerializedName("chart")
    var chart: List<Any>? = listOf(),
    @SerializedName("order_num")
    var orderNum: Int? = 0,
    @SerializedName("order_price")
    var orderPrice: Double? = 0.0
)