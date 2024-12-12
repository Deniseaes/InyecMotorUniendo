package front.inyecmotor.productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import front.inyecmotor.crearProducto.TipoDTO;

public class TipoSpinnerAdapter extends ArrayAdapter<TipoDTO> {

    public TipoSpinnerAdapter(Context context, List<TipoDTO> tipos) {
        super(context, android.R.layout.simple_spinner_item, tipos);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(android.R.id.text1);
        TipoDTO currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getNombre());
        }

        return convertView;
    }
}