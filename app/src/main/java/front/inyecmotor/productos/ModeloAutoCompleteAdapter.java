package front.inyecmotor.productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import front.inyecmotor.R;
import front.inyecmotor.modelos.Modelo;

import java.util.List;

public class ModeloAutoCompleteAdapter extends ArrayAdapter<Modelo> {
    private LayoutInflater inflater;

    public ModeloAutoCompleteAdapter(Context context, List<Modelo> modelos) {
        super(context, R.layout.modelo_item, modelos);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.modelo_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.modelo_nombre);
        Modelo modelo = getItem(position);
        if (modelo != null) {
            textView.setText(modelo.getNombre());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}