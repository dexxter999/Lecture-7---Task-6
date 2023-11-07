package com.example.useradd

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.useradd.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val users = mutableListOf<User>()
    private val deletedUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.buttonAddUser.setOnClickListener {
            userRegistration()
        }

        binding.buttonDeleteUser.setOnClickListener {
            deleteUser()
            displayDeletedUsers()
        }

        binding.buttonUpdateUser.setOnClickListener {
            updateUserInformation()
        }

    }

    private fun userRegistration() {
        if (validateEditTexts()) {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val age = binding.age.text.toString()
            val email = binding.email.text.toString()

            if (users.any { it.email == email }) {
                Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show()
                binding.tvError.setTextColor(Color.RED)
            } else if (age.startsWith('0')) {
                Toast.makeText(this, "Age must not start with 0!", Toast.LENGTH_SHORT).show()
                binding.tvError.setTextColor(Color.RED)
            } else {
                val user = User(firstName, lastName, age, email)
                addUser(user)
                displayUsers()
                Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show()
                clearEditTexts()
                binding.tvSuccess.setTextColor(Color.GREEN)

            }


        }

    }

    private fun deleteUser() {
        val email = binding.email.text.toString()

        val existingUser = users.find { it.email == email }
        existingUser?.apply {
            users.remove(existingUser)
            deletedUsers.add(existingUser)
            displayUsers()
            displayDeletedUsers()
            clearEditTexts()
            Toast.makeText(this@MainActivity, "User deleted successfully!", Toast.LENGTH_SHORT)
                .show()
            binding.tvSuccess.setTextColor(Color.GREEN)

        } ?: kotlin.run {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
            binding.tvError.setTextColor(Color.RED)

        }
    }


    private fun emailValidation(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun updateUserInformation() {
        val email = binding.email.text.toString()
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val age = binding.age.text.toString()

        val existingUserUpdate = users.find { it.email == email }

        existingUserUpdate?.apply {
            existingUserUpdate.name = firstName
            existingUserUpdate.lastName = lastName
            existingUserUpdate.age = age
            displayUsers()
            clearEditTexts()
            Toast.makeText(this@MainActivity, "User updated successfully!", Toast.LENGTH_SHORT)
                .show()
            binding.tvSuccess.setTextColor(Color.GREEN)
        } ?: kotlin.run {
            Toast.makeText(this, "User does not exists.", Toast.LENGTH_SHORT).show()
            binding.tvError.setTextColor(Color.RED)
        }
    }

    private fun displayDeletedUsers() {
        val deletedUsers = deletedUsers.count()
        binding.deletedUsersCount.text = deletedUsers.toString()
    }

    private fun displayUsers() {
        val usersText = users.count()
        binding.activeUsersCount.text = usersText.toString()
    }

    private fun clearEditTexts() {
        binding.firstName.text.clear()
        binding.lastName.text.clear()
        binding.age.text.clear()
        binding.email.text.clear()
    }


    private fun addUser(user: User) {
        users.add(user)
    }


    private fun validateEditTexts(): Boolean {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val age = binding.age.text.toString()
        val email = binding.email.text.toString()

        if (firstName.isEmpty()) {
            Toast.makeText(this, "First name cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, "Last name cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (age.isEmpty()) {
            Toast.makeText(this, "Age cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isNotEmpty()) {
            if (!emailValidation(email)) {
                Toast.makeText(this, "Enter Email correctly!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
        }


        return true
    }


}


