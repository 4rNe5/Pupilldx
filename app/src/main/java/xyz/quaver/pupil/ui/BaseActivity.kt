/*
 *     Pupil, Hitomi.la viewer for Android
 *     Copyright (C) 2020  tom5079
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.quaver.pupil.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import xyz.quaver.pupil.R
import xyz.quaver.pupil.util.LockManager
import xyz.quaver.pupil.util.Preferences
import xyz.quaver.pupil.util.normalizeID

open class BaseActivity : AppCompatActivity() {

    private var locked: Boolean = true

    private val lockLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK)
            locked = false
        else
            finish()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        locked = !LockManager(this).locks.isNullOrEmpty()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()

        if (Preferences["security_mode"])
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        if (locked)
            lockLauncher.launch(Intent(this, LockActivity::class.java))
    }

}