package com.example.dialogstudentmanagement

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import android.widget.*

class MainActivity : ComponentActivity() {
    private lateinit var listView: ListView
    private lateinit var rootLayout: LinearLayout
    private lateinit var adapter: ArrayAdapter<String>
    private val studentList = mutableListOf<String>()
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        rootLayout = findViewById(R.id.rootLayout)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        listView.adapter = adapter

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            showStudentDialog(isEdit = false)
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            if (selectedPosition in studentList.indices) {
                showStudentDialog(isEdit = true)
            } else {
                Toast.makeText(this, "Vui lòng chọn sinh viên để cập nhật", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (selectedPosition in studentList.indices) {
                showDeleteConfirmation()
            } else {
                Toast.makeText(this, "Vui lòng chọn sinh viên để xóa", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
            Toast.makeText(this, "Đã chọn: ${studentList[position]}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showStudentDialog(isEdit: Boolean) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_student, null)
        val nameField = dialogView.findViewById<EditText>(R.id.dialogEtName)
        val mssvField = dialogView.findViewById<EditText>(R.id.dialogEtMSSV)

        if (isEdit && selectedPosition in studentList.indices) {
            val parts = studentList[selectedPosition].split(" - ")
            if (parts.size == 2) {
                nameField.setText(parts[0])
                mssvField.setText(parts[1])
            }
        }

        val dialogTitle = if (isEdit) "Cập nhật sinh viên" else "Thêm sinh viên"

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val name = nameField.text.toString().trim()
                val mssv = mssvField.text.toString().trim()
                if (name.isNotEmpty() && mssv.isNotEmpty()) {
                    val studentEntry = "$name - $mssv"
                    if (isEdit && selectedPosition in studentList.indices) {
                        studentList[selectedPosition] = studentEntry
                        Toast.makeText(this, "Đã cập nhật sinh viên", Toast.LENGTH_SHORT).show()
                    } else {
                        studentList.add(studentEntry)
                        Toast.makeText(this, "Đã thêm sinh viên", Toast.LENGTH_SHORT).show()
                    }
                    adapter.notifyDataSetChanged()
                    selectedPosition = -1
                } else {
                    Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDeleteConfirmation() {
        val student = studentList[selectedPosition]
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa sinh viên: $student?")
            .setPositiveButton("Xóa") { _, _ ->
                studentList.removeAt(selectedPosition)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
                selectedPosition = -1
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}