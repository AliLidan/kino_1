package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import com.example.kino.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // добавляем в главную активность ресурс-файл показа меню
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu, menu);   //конвертирeтся созданный нами ресурс "меню" в программный объект
        return super.onCreateOptionsMenu(menu); // создаем функцию  //вывод меню при нажатии кнопки
    }

    public boolean onOptionsItemSelected(MenuItem item) { // тут идет проверка на какую "кнопку" нажал пользователь
        // TODO Auto-generated method stub                //чтобы перейти в другую активность
        Intent intent =null;
        switch (item.getItemId())                         // проверка через switch по id элемента
        {
            case R.id.m1: intent = new Intent(this, theatres.class);
                break;
            case R.id.m2: intent = new Intent(this, films.class);
                break;
            case R.id.m3: intent = new Intent(this, theatres_films.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent =null;
        switch (v.getId())
        {
            case R.id.button2:intent = new Intent(this, new_theatres.class);
                break;
            case R.id.button3:intent = new Intent(this, update_delete.class);
                break;
            case R.id.button4:intent = new Intent(this, new_films.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }


}