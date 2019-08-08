package com.QUeM.TreGStore.DatabaseClass;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.QUeM.TreGStore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProdottiAdapter extends FirestoreRecyclerAdapter<Prodotti, ProdottiAdapter.ProdottiHolder> {

    public ProdottiAdapter(FirestoreRecyclerOptions<Prodotti> options) {
        super(options);
    }

    /***
     * metodo che prende e mostra i dati richiesti
     * @param holder contiene gli elementi della Cardview da impostare con i valori delle occorrenze dei documenti
     * @param position
     * @param model contiene i dati del documento preso in un oggetto
     */
    @Override
    protected void onBindViewHolder(ProdottiHolder holder, int position, Prodotti model) {
        //selezione per non mostrare un prodotto nullo
        if(model.getId()!=null){
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
        }else{
            //se id è nullo imposto tutta la CardView a null per non farla comparire a schermo
            holder.nome.setText("");
            holder.nProdottiCarrello.setText("");
            holder.prezzo.setText("");
            holder.prezzoCad.setText("");
            holder.icona.setVisibility(View.INVISIBLE);
        }

    }

    @NonNull
    @Override
    public ProdottiHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //crea la vista della lista
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prodotti_item_recyclerview, viewGroup, false);
        return new ProdottiHolder(v);
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
