package helper

import java.sql.Timestamp
import java.util.Date

object GlobalSettingHelper {
    val startUpTime = new Timestamp(new Date().getTime)
}
