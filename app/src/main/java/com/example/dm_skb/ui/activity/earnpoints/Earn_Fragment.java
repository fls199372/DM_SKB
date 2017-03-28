package com.example.dm_skb.ui.activity.earnpoints;



import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.activity.fragment.BaseFragment;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.activity.newcustomer.CardPicturesActivity;
import com.example.dm_skb.ui.activity.newcustomer.NewCustomerActivity;


public class Earn_Fragment extends BaseFragment  {

    Button card;
    Button evaluate;
    Button correction;
    FragmentTransaction  fm;
    Button report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.earn_main, container, false);
    }
    /**
     * 获取xml控件
     */
   
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        report=(Button) view.findViewById(R.id.report);
        report.setOnClickListener(clickListener);
        evaluate=(Button) view.findViewById(R.id.evaluate);
        evaluate.setOnClickListener(clickListener);
        correction=(Button) view.findViewById(R.id.correction);
        correction.setOnClickListener(clickListener);
        card=(Button) view.findViewById(R.id.card);
        card.setOnClickListener(clickListener);
	}
    public View.OnClickListener clickListener=new View.OnClickListener() {

        public void onClick(View v) {
            if(v==report)
            {
                startActivity(new Intent(getActivity(), NewCustomerActivity.class));

            }else if(v==evaluate){
                Intent intent=new Intent(getActivity(),StartActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", "2");
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);

            }else if(v==correction){
                //Toast.makeText(getActivity(),"2", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),StartActivity.class);
                Bundle bundle=new Bundle();

                bundle.putString("id", "3");

                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }else if(v==card)
            {
                startActivity(new Intent(getActivity(), CardPicturesActivity.class));
            }

        }
    };

}
