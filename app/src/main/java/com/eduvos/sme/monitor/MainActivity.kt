package com.eduvos.sme.monitor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eduvos.sme.monitor.data.AppDatabase
import com.eduvos.sme.monitor.data.Repository
import com.eduvos.sme.monitor.model.PipelineJob
import com.eduvos.sme.monitor.model.SMEClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private lateinit var smeValue: TextView
    private lateinit var pipelineValue: TextView
    private lateinit var clientListContainer: LinearLayout
    private lateinit var jobListContainer: LinearLayout

    private lateinit var etClientName: EditText
    private lateinit var etClientNeed: EditText
    private lateinit var etJobTitle: EditText
    private lateinit var etJobAmount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Repository(AppDatabase.getDatabase(this))

        val scroll = ScrollView(this)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 60, 32, 32)
        }

        // HEADER
        root.addView(TextView(this).apply { text = "SME Pipeline Monitor"; textSize = 20f; setPadding(0,0,0,20) })
        smeValue = TextView(this).apply { textSize = 16f }
        pipelineValue = TextView(this).apply { textSize = 16f }
        root.addView(smeValue)
        root.addView(pipelineValue)

        // --- CLIENT INPUT ---
        root.addView(TextView(this).apply { text = "\n--- ADD CLIENT ---"; textSize = 16f })
        etClientName = EditText(this).apply { hint = "Client Name (e.g. Mrs Khumalo)" }
        etClientNeed = EditText(this).apply { hint = "What they want (e.g. Leaking geyser)" }
        val btnAddClient = Button(this).apply { text = "ADD NEW CLIENT" }
        root.addView(etClientName); root.addView(etClientNeed); root.addView(btnAddClient)

        // --- JOB INPUT ---
        root.addView(TextView(this).apply { text = "\n--- ADD JOB ---"; textSize = 16f })
        etJobTitle = EditText(this).apply { hint = "Job (e.g. Install geyser)" }
        etJobAmount = EditText(this).apply { hint = "Amount (e.g. 3000)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val btnAddJob = Button(this).apply { text = "ADD NEW JOB - e.g. R3000" }
        root.addView(etJobTitle); root.addView(etJobAmount); root.addView(btnAddJob)

        // --- DASHBOARD LISTS ---
        root.addView(TextView(this).apply { text = "\n--- DASHBOARD: Clients ---"; textSize = 18f })
        clientListContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        root.addView(clientListContainer)

        root.addView(TextView(this).apply { text = "\n--- DASHBOARD: Jobs ---"; textSize = 18f })
        jobListContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        root.addView(jobListContainer)

        val btnViewDashboard = Button(this).apply { text = "REFRESH DASHBOARD" }
        val btnLogout = Button(this).apply { text = "LOGOUT" }
        root.addView(btnViewDashboard); root.addView(btnLogout)

        scroll.addView(root)
        setContentView(scroll)

        updateDashboard()

        btnAddClient.setOnClickListener {
            val name = etClientName.text.toString()
            val need = etClientNeed.text.toString()
            if (name.isEmpty()) { Toast.makeText(this, "Enter client name", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                repository.insertClient(SMEClient(name = name, email = need, businessType = need))
                etClientName.text.clear(); etClientNeed.text.clear()
                Toast.makeText(this@MainActivity, "Client Added: $name - $need", Toast.LENGTH_SHORT).show()
                updateDashboard()
            }
        }

        btnAddJob.setOnClickListener {
            val title = etJobTitle.text.toString()
            val amt = etJobAmount.text.toString().toDoubleOrNull() ?: 0.0
            if (title.isEmpty()) { Toast.makeText(this, "Enter job title", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                repository.insertJob(PipelineJob(jobTitle = title, status = "In Progress", amount = amt))
                etJobTitle.text.clear(); etJobAmount.text.clear()
                Toast.makeText(this@MainActivity, "Job Added: $title R$amt", Toast.LENGTH_SHORT).show()
                updateDashboard()
            }
        }

        btnViewDashboard.setOnClickListener { updateDashboard() }
        btnLogout.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)); finish() }
    }

    private fun updateDashboard() {
        lifecycleScope.launch {
            val clients = repository.getAllClients()
            val jobs = repository.getAllJobs()
            smeValue.text = "Clients: ${clients.size}"
            pipelineValue.text = "Jobs: ${jobs.size} | Total Value: R${jobs.sumOf { it.amount }}"

            // Render Clients with SPECIFIC delete
            clientListContainer.removeAllViews()
            clients.forEach { client ->
                val row = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
                row.addView(TextView(this@MainActivity).apply { text = "${client.name} - ${client.businessType}"; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f) })
                val del = Button(this@MainActivity).apply { text = "X"; textSize = 10f }
                del.setOnClickListener { lifecycleScope.launch { repository.deleteClient(client); updateDashboard() } }
                row.addView(del)
                clientListContainer.addView(row)
            }

            // Render Jobs with SPECIFIC delete
            jobListContainer.removeAllViews()
            jobs.forEach { job ->
                val row = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
                row.addView(TextView(this@MainActivity).apply { text = "${job.jobTitle} - R${job.amount}"; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f) })
                val del = Button(this@MainActivity).apply { text = "Done"; textSize = 10f }
                del.setOnClickListener { lifecycleScope.launch { repository.deleteJob(job); Toast.makeText(this@MainActivity, "Removed: ${job.jobTitle}", Toast.LENGTH_SHORT).show(); updateDashboard() } }
                row.addView(del)
                jobListContainer.addView(row)
            }
        }
    }
}