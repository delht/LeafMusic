package vn.edu.stu.leafmusic.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import vn.edu.stu.leafmusic.R;

public class SearchFragment extends Fragment {

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = rootView.findViewById(R.id.search_view);

        // Tùy chỉnh màu icon kính lúp trắng
        int whiteColor = ContextCompat.getColor(getContext(), android.R.color.white);
        int blackColor = ContextCompat.getColor(getContext(), android.R.color.black);

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        if (searchIcon != null) {
            searchIcon.setColorFilter(whiteColor); // Màu trắng cho kính lúp
        }

        // Đặt màu trắng cho chữ khi nhập và gợi ý
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setTextColor(blackColor); // Màu chữ khi người dùng nhập
            searchEditText.setHintTextColor(whiteColor); // Màu chữ gợi ý
            searchEditText.setHint("Tìm kiếm bài hát...");
        }

        return rootView;
    }
}