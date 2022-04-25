package com.hoshi.wifitransfer

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hoshi.wifitransfer.databinding.ItemFileBinding

/**
 * Created by lv.qx on 2022/4/25
 */
class FileAdapter : BaseQuickAdapter<FileBean, FileAdapter.FileHolder>(0) {

    override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FileHolder(ItemFileBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun convert(holder: FileHolder, item: FileBean) {
        val binding = holder.binding
        binding.ivIcon.load(item.icon)
        binding.tvName.text = item.name
        binding.tvPath.text = item.path
        binding.tvSize.text = item.size
    }

    class FileHolder(val binding: ItemFileBinding) : BaseViewHolder(binding.root)

}