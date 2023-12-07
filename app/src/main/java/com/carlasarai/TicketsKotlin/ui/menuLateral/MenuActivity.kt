package com.carlasarai.TicketsKotlin.ui.menuLateral

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carlasarai.TicketsKotlin.ui.canjearComensal.CanjearComensalFragment
import com.carlasarai.TicketsKotlin.ui.canjearComensal.CanjearComensalViewModel
import com.carlasarai.TicketsKotlin.ui.obtenerCsv.ObtenerCsvActivity
import com.carlasarai.pruebanueva.R
import com.carlasarai.pruebanueva.databinding.ActivityMenuBinding
import com.google.android.material.navigation.NavigationView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


class MenuActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding
    private val menuActivityViewModel: MenuActivityViewModel by viewModels()
    private val canjearComensalViewModel: CanjearComensalViewModel by viewModels()
    private lateinit var createFileLauncher: ActivityResultLauncher<Intent>
    private lateinit var createFileInformationLauncher: ActivityResultLauncher<Intent>
    private val PERMISSION_REQUEST_CODE = 123
    private var permissionsExplanationShown = false
    private var informeGuardato = false
    private var baseDatosGuardada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_fragment_content_menu)
        val toolbar: Toolbar = binding.appBarMenu.toolbar

        setSupportActionBar(toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_canjear_comensal, R.id.nav_canjear_comensal_por_nombre_area, R.id.nav_comensales_canjeados, R.id.nav_comensales_disponibles, R.id.nav_menu_consumido
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        menuActivityViewModel.getTicks.observe(this) { ticketList ->
            if (ticketList.isEmpty()) {
                menuActivityViewModel.isLoading.postValue(false)
                menuActivityViewModel.overlayView.postValue(false)
                getToBuscarCsv()
                finish()
            }
        }
        menuActivityViewModel.llenarListas(this)

        createFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //El código para manejar el resultado
                result.data?.data?.let { uri ->
                    // Notificar al ViewModel sobre la selección del archivo
                    menuActivityViewModel.onSeleccionArchivo(uri, this, this)
                }
            }
        }

        createFileInformationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //El código para manejar el resultado
                result.data?.data?.let { uri ->
                    // Notificar al ViewModel sobre la selección del archivo
                    menuActivityViewModel.onSeleccionArchivoInformacion(uri, this)
                }
            }
        }

        menuActivityViewModel.isLoading.observe(this, Observer {
            binding.progress.isVisible = it
        })
        menuActivityViewModel.overlayView.observe(this, Observer {
            binding.overlayView.isVisible = it
        })
    }

    override fun onStart() {
        super.onStart()
        if (!menuActivityViewModel.hasStoragePermission(this)){
            permissionsExplanationShown = false
        }
        menuActivityViewModel.permissionReadExternalStorage(this)
    }

    fun getToBuscarCsv() {
        val intent = Intent(this, ObtenerCsvActivity::class.java)
        startActivity(intent)
        finish() // Finalizar la actividad actual y va a (obtenerCsv)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scanerQr -> {
                // Lógica para el botón scanerQr
                barcodeLauncher.launch(menuActivityViewModel.initScanner())
                return true
            }
            R.id.exportarArchivo -> {
                // Lógica para el botón exportarArchivo
                if (menuActivityViewModel.isLoading.value == false){
                    if (menuActivityViewModel.hasStoragePermission(this)) {
                        val dialog = AlertDialog.Builder(this)
                            .setMessage("Guardar el listado de los tickets consumidos y sin consumir ")
                            .setPositiveButton("Sí") { _, _ ->
                                val fileName = "ListaCtrlTickets.csv"
                                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_TITLE, fileName)
                                }
                                createFileLauncher.launch(intent)
                                baseDatosGuardada = true
                                Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("No") { _, _ ->
                                // Manejar la cancelación o realizar otra acción específica
                            }
                            .create()
                        dialog.show()
                    }else{
                        menuActivityViewModel.permissionReadExternalStorage(this)
                    }
                }
                return true
            }
            R.id.exportarInforme -> {
                if(menuActivityViewModel.isLoading.value == false){
                    if (menuActivityViewModel.hasStoragePermission(this)){
                        val dialog = AlertDialog.Builder(this)
                            .setMessage("Guardar el informe diario")
                            .setPositiveButton("Sí") { _, _ ->
                                val fileInformationName = "InformeDiario.csv"
                                val intentInformacion = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_TITLE, fileInformationName)
                                }
                                createFileInformationLauncher.launch(intentInformacion)
                                informeGuardato = true
                                Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("No") { _, _ ->
                                // Manejar la cancelación o realizar otra acción específica de tu aplicación
                            }
                            .create()
                        dialog.show()
                    }else{
                        menuActivityViewModel.permissionReadExternalStorage(this)
                    }
                }
                return true
            }
            R.id.terminar -> {
                if (baseDatosGuardada){
                    if (informeGuardato){
                        val dialog = AlertDialog.Builder(this)
                            .setMessage("¿Estás seguro de terminar la confirmacion de tickets?")
                            .setPositiveButton("Sí") { _, _ ->
                                menuActivityViewModel.mantenerLista.postValue(0)
                                menuActivityViewModel.deleteAll()
                                Toast.makeText(this, "Datos Vaciados", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("No") { _, _ ->
                                // Manejar la cancelación o realizar otra acción específica de tu aplicación
                            }
                            .create()
                        dialog.show()
                    }else{
                        Toast.makeText(this, "Primero debes guardar el informe diario", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Primero debes guardar el listado de tickets", Toast.LENGTH_LONG).show()
                }
                return true
            }
            // Agrega más casos según sea necesario
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
        } else {
            //Toast.makeText(this, "Escaneado exitosamente: ${result.contents}", Toast.LENGTH_LONG).show()
            canjearComensalViewModel.numero_codigo_qr.postValue(result.contents)
            val bundle = Bundle()
            bundle.putInt("numeroTicketQr", result.contents.toInt())
            val canjearComensalFragment = CanjearComensalFragment()
            canjearComensalFragment.arguments = bundle
            // Reemplazar el fragmento actual con CanjearComensalFragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.drawer_layout, canjearComensalFragment)
            transaction.addToBackStack("HOLA")
            transaction.setReorderingAllowed(true)
            transaction.commit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(this, "Gracias por conceder el permiso.", Toast.LENGTH_SHORT).show()
                permissionsExplanationShown = true
            } else {
                // Permiso no concedido
                if (!permissionsExplanationShown) {
                    showPermissionsExplanationDialog()
                } else {
                    Toast.makeText(this, "No se aceptaron los permisos.", Toast.LENGTH_SHORT).show()
                    permissionsExplanationShown = false
                }
            }
        }
    }

    private fun showPermissionsExplanationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Permisos necesarios")
            .setMessage("La aplicación necesita acceso al almacenamiento guardar el listado de los tickets y el informe diario.")
            .setPositiveButton("Intentar de nuevo") { _, _ ->
                permissionsExplanationShown = true
                menuActivityViewModel.permissionReadExternalStorage(this)
            }
            .setNegativeButton("Cancelar") { _, _ ->
                // Manejar la cancelación o realizar otra acción específica de tu aplicación
            }
            .create()
        dialog.show()
    }
}