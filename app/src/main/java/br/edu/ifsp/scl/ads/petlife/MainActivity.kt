package br.edu.ifsp.scl.ads.petlife

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.petlife.databinding.ActivityMainBinding

data class Pet(
    val nome: String,
    val dataNascimento: String,
    val tipo: String,
    val cor: String,
    val porte: String,
    var ultimaIdaPetShop: String,
    var ultimaIdaVeterinario: String,
    var ultimaIdaVacina: String
)

class MainActivity : AppCompatActivity() {

    private lateinit var amb: ActivityMainBinding
    private val listaPets = mutableListOf<Pet>()

    // Launchers para editar as datas
    private lateinit var editarIdaVeterinarioLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarIdaPetshopLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarIdaVacinaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amb.root)

        amb.salvarPetBt.setOnClickListener {
            salvarPet()
        }

        amb.editarIdaVeterinarioBt.setOnClickListener {
            editarIdaVeterinario()
        }

        amb.editarIdaPetshopBt.setOnClickListener {
            editarIdaPetshop()
        }

        amb.editarIdaVacinaBt.setOnClickListener {
            editarIdaVacina()
        }

        // Launchers para cada uma das Activities
        editarIdaVeterinarioLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val nomeCachorro = result.data?.getStringExtra("nomeCachorro")
                val novaData = result.data?.getStringExtra("novaDataVeterinario")
                if (nomeCachorro != null && novaData != null) {
                    atualizarDataVeterinario(nomeCachorro, novaData)
                }
            }
        }

        editarIdaPetshopLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val nomeCachorro = result.data?.getStringExtra("nomeCachorro")
                val novaData = result.data?.getStringExtra("novaDataPetshop")
                if (nomeCachorro != null && novaData != null) {
                    atualizarDataPetshop(nomeCachorro, novaData)
                }
            }
        }

        editarIdaVacinaLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val nomeCachorro = result.data?.getStringExtra("nomeCachorro")
                val novaData = result.data?.getStringExtra("novaDataVacina")
                if (nomeCachorro != null && novaData != null) {
                    atualizarDataVacina(nomeCachorro, novaData)
                }
            }
        }
    }

    private fun salvarPet() {
        val nome = amb.nomePetEt.text.toString()
        val dataNascimento = amb.dataNascimentoEt.text.toString()
        val tipo = amb.tipoEt.text.toString()
        val cor = amb.corEt.text.toString()
        val porte = amb.porteEt.text.toString()
        val ultimaIdaPetShop = amb.ultimaIdaPetShopEt.text.toString()
        val ultimaIdaVeterinario = amb.ultimaIdaVeterinarioEt.text.toString()
        val ultimaIdaVacina = amb.ultimaIdaVacinaEt.text.toString()

        if (nome.isEmpty() || dataNascimento.isEmpty() || tipo.isEmpty() || cor.isEmpty() || porte.isEmpty() ||
            ultimaIdaPetShop.isEmpty() || ultimaIdaVeterinario.isEmpty() || ultimaIdaVacina.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        } else {
            val pet = Pet(
                nome,
                dataNascimento,
                tipo,
                cor,
                porte,
                ultimaIdaPetShop,
                ultimaIdaVeterinario,
                ultimaIdaVacina
            )
            listaPets.add(pet)
            atualizarListaPets()
            limparCampos()
            Toast.makeText(this, "Pet salvo com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun atualizarListaPets() {
        val stringBuilder = StringBuilder()
        listaPets.forEachIndexed { index, pet ->
            stringBuilder.append("Pet: ${index + 1}\nNome: ${pet.nome}\nData de Nascimento: ${pet.dataNascimento}\nTipo: ${pet.tipo}\nCor: ${pet.cor}\nPorte: ${pet.porte}\nÚltima Ida ao PetShop: ${pet.ultimaIdaPetShop}\nÚltima Ida ao Veterinário: ${pet.ultimaIdaVeterinario}\nÚltima Ida para Vacina: ${pet.ultimaIdaVacina}\n\n")
        }
        amb.listaPetsTv.text = stringBuilder.toString()
    }

    private fun limparCampos() {
        amb.nomePetEt.text.clear()
        amb.dataNascimentoEt.text.clear()
        amb.tipoEt.text.clear()
        amb.corEt.text.clear()
        amb.porteEt.text.clear()
        amb.ultimaIdaPetShopEt.text.clear()
        amb.ultimaIdaVeterinarioEt.text.clear()
        amb.ultimaIdaVacinaEt.text.clear()
    }

    private fun editarIdaVeterinario() {
        val intent = Intent(this, EditarIdaAoVeterinarioActivity::class.java)
        editarIdaVeterinarioLauncher.launch(intent)
    }

    private fun editarIdaPetshop() {
        val intent = Intent(this, EditarIdaAoPetshopActivity::class.java)
        editarIdaPetshopLauncher.launch(intent)
    }

    private fun editarIdaVacina() {
        val intent = Intent(this, EditarIdaParaVacinaActivity::class.java)
        editarIdaVacinaLauncher.launch(intent)
    }

    // Métodos para atualizar as datas nas listas com base no nome do cachorro
    private fun atualizarDataVeterinario(nomeCachorro: String, novaData: String) {
        val pet = listaPets.find { it.nome == nomeCachorro }
        pet?.let {
            it.ultimaIdaVeterinario = novaData
            atualizarListaPets()
            Toast.makeText(this, "Data de ida ao veterinário atualizada!", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, "Pet não encontrado!", Toast.LENGTH_SHORT).show()
    }

    private fun atualizarDataPetshop(nomeCachorro: String, novaData: String) {
        val pet = listaPets.find { it.nome == nomeCachorro }
        pet?.let {
            it.ultimaIdaPetShop = novaData
            atualizarListaPets()
            Toast.makeText(this, "Data de ida ao Petshop atualizada!", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, "Pet não encontrado!", Toast.LENGTH_SHORT).show()
    }

    private fun atualizarDataVacina(nomeCachorro: String, novaData: String) {
        val pet = listaPets.find { it.nome == nomeCachorro }
        pet?.let {
            it.ultimaIdaVacina = novaData
            atualizarListaPets()
            Toast.makeText(this, "Data de vacinação atualizada!", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, "Pet não encontrado!", Toast.LENGTH_SHORT).show()
    }
}
