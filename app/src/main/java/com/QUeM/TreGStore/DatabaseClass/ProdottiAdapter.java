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

    @Override
    protected void onBindViewHolder(ProdottiHolder holder, int position, Prodotti model) {
        if(model.getId()!=null){

            holder.nome.setText(model.getNome());
            holder.nProdottiCarrello.setText("x"+model.getTotalePezziCarrello());
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
                    holder.icona.setImageResource(R.drawable.prodotto_latte);
                    break;
            }
        }else{
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
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prodotti_item_recyclerview, viewGroup, false);

        return new ProdottiHolder(v);
    }

    class ProdottiHolder extends RecyclerView.ViewHolder{

        TextView nome;
        TextView prezzo;
        TextView nProdottiCarrello;
        TextView prezzoCad;
        ImageView icona;


        public ProdottiHolder(@NonNull View itemView) {
            super(itemView);
            nome=itemView.findViewById(R.id.text_view_nome_prodotto);
            prezzo=itemView.findViewById(R.id.text_view_prezzo);
            nProdottiCarrello=itemView.findViewById(R.id.text_view_n_prodotti_nel_carrello);
            icona=itemView.findViewById(R.id.image_view_prodotto);
            prezzoCad=itemView.findViewById(R.id.text_view_cad);
        }
    }


}
