package com.QUeM.TreGStore.DatabaseClass;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.QUeM.TreGStore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;

public class ProdottiAdapter extends FirestoreRecyclerAdapter<Prodotti, ProdottiAdapter.ProdottiHolder> {

    public ProdottiAdapter(FirestoreRecyclerOptions<Prodotti> options) {
        super(options);
    }

    //ottengo le info riguardo l'utente connesso
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    /***
     * metodo che prende e mostra i dati richiesti
     * @param holder contiene gli elementi della Cardview da impostare con i valori delle occorrenze dei documenti
     * @param position
     * @param model contiene i dati del documento preso in un oggetto
     */
    @Override
    protected void onBindViewHolder(ProdottiHolder holder, int position, Prodotti model) {
        //selezione per non mostrare un prodotto nullo
        //se il prodotto ha un id diverso da null imposto i dati nel punto
        holder.nome.setText(model.getNome());
        holder.nProdottiCarrello.setText("x"+model.getTotalePezziCarrello());
        //metodo per arrotondare alla seconda cifra decimale
        double roundOff = Math.round((model.getPrezzo()*model.getTotalePezziCarrello()) * 100.0) / 100.0;
        holder.prezzo.setText(String.valueOf(roundOff)+"€");
        if(model.getTotalePezziCarrello()<2){
            holder.prezzoCad.setText("");
        }else{
            holder.prezzoCad.setText(String.valueOf(model.getPrezzo())+"/cad");
        }
        switch(model.getNome()){
            //per impostare l'immagine
                case "Nutella":
                    holder.icona.setImageResource(R.drawable.prodotto_nutella);
                    break;
                case "Latte Parmalat":
                    holder.icona.setImageResource(R.drawable.prodotto_latte);
                    break;
                case "Insalata Cappuccio":
                    holder.icona.setImageResource(R.drawable.prodotto_cavolo_cappuccio);
                    break;
                case "Grisbì":
                    holder.icona.setImageResource(R.drawable.prodotto_grisbi);
                    break;
                case "Prosciutto Crudo di Parma":
                    holder.icona.setImageResource(R.drawable.prodotto_prosciutto);
                    break;
                case "Grana Padano":
                    holder.icona.setImageResource(R.drawable.prodotto_formaggio);
                    break;
                case "Insalata Iceberg":
                    holder.icona.setImageResource(R.drawable.prodotto_iceberg);
                    break;
                case "Pringles Ketchup":
                    holder.icona.setImageResource(R.drawable.prodotto_pringles);
                    break;
                case "Birra Peroni":
                    holder.icona.setImageResource(R.drawable.prodotto_peroni);
                    break;
                case "Macine":
                    holder.icona.setImageResource(R.drawable.prodotto_macine);
                    break;
                case "Caffè Lavazza":
                    holder.icona.setImageResource(R.drawable.prodotto_caffe);
                    break;
            }
    }

    @NonNull
    @Override
    public ProdottiHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //crea la vista della lista
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prodotti_item_recyclerview, viewGroup, false);
        return new ProdottiHolder(v);
    }

    //metodo per eliminare un elemento dalla recycler view
    public void deleteItem(final int position){
        //metodo che cancella il prodotto su cui si è effettuato lo swipe verso sinistra
        DocumentReference prodottoDaCancellare=getSnapshots().getSnapshot(position).getReference();
        prodottoDaCancellare.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Prodotti prod=document.toObject(Prodotti.class);
                        //faccio update del conto nel carrello
                        aggiornaContoCarrello(prod.getPrezzo()*prod.getTotalePezziCarrello());
                        //cancello effettivamente il prodotto dal carrello
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }else{
                        Log.d(TAG, "No such document");
                    }
                }else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //tolgo il prezzo del prodotto dal totale del carrello
    public void aggiornaContoCarrello(final double totaleDaTogliere){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference totCarrelloRef=db.collection("conti").document(auth.getUid());
        totCarrelloRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se la connessione è riuscita, vedo se il documento esiste o meno
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Conti contoDaAggiornare=document.toObject(Conti.class);
                        double roundOff = Math.round((contoDaAggiornare.getTotaleCarrello()-totaleDaTogliere) * 100.0) / 100.0;
                        //se il documento esiste, creo un oggetto che corrisponde al prodotto legato al codice a barre
                        totCarrelloRef.update("totaleCarrello", roundOff);

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    class ProdottiHolder extends RecyclerView.ViewHolder{

        //creo gli oggetti che contengono le info da mostrare
        TextView nome;
        TextView prezzo;
        TextView nProdottiCarrello;
        TextView prezzoCad;
        ImageView icona;


        public ProdottiHolder(@NonNull View itemView) {
            super(itemView);
            //inizializzo le variabili con gli elementi della CardView
            nome=itemView.findViewById(R.id.text_view_nome_prodotto);
            prezzo=itemView.findViewById(R.id.text_view_prezzo);
            nProdottiCarrello=itemView.findViewById(R.id.text_view_n_prodotti_nel_carrello);
            icona=itemView.findViewById(R.id.image_view_prodotto);
            prezzoCad=itemView.findViewById(R.id.text_view_cad);
        }
    }


}
