import android.text.TextUtils
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.view.animation.AlphaAnimation


fun TextView.marqueeOnceThenFadeToEllipsizeEnd(
    owner: LifecycleOwner,
    startDelay: Long = 2000L,      // Задержка перед началом marquee
    marqueeDuration: Long = 7000L, // Примерное время прокрутки
    fadeDuration: Long = 700L      // Время анимации
) {
    owner.lifecycleScope.launch {
        // Задержка перед началом
        delay(startDelay)

        // Запуск marquee
        this@marqueeOnceThenFadeToEllipsizeEnd.apply {
            ellipsize = TextUtils.TruncateAt.MARQUEE
            isSelected = true
            marqueeRepeatLimit = 1
        }

        // Ожидание окончания прокрутки
        delay(marqueeDuration)

        // Анимация затухания
        val fadeOut = AlphaAnimation(1f, 0f).apply {
            duration = fadeDuration
            fillAfter = true
        }
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = fadeDuration
            fillAfter = true
        }

        // Затухание → смена режима → возврат
        this@marqueeOnceThenFadeToEllipsizeEnd.startAnimation(fadeOut)
        delay(fadeDuration)

        this@marqueeOnceThenFadeToEllipsizeEnd.apply {
            ellipsize = TextUtils.TruncateAt.END
            isSelected = false
        }

        this@marqueeOnceThenFadeToEllipsizeEnd.startAnimation(fadeIn)
    }
}


