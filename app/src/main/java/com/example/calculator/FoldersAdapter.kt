package com.example.calculator

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.FoldersViewBinding
import com.example.calculator.databinding.VideoViewBinding

class FoldersAdapter(private val context: Context, private var foldersList: ArrayList<Folder>) : RecyclerView.Adapter<FoldersAdapter.MyHolder>(){
    class MyHolder(binding: FoldersViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val folderName = binding.FolderNameFV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FoldersViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.folderName.text = foldersList[position].folderName
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }

}