package com.frxhaikal_plg.ingrevia.ui.introduction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroductionPagerAdapter(
    activity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {
    
    override fun getItemCount(): Int = fragments.size
    
    override fun createFragment(position: Int): Fragment = fragments[position]
}
