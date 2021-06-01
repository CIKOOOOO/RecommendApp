package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yosua.recommendapp.model.Data;
import com.yosua.recommendapp.model.Edge;
import com.yosua.recommendapp.model.Graph;
import com.yosua.recommendapp.model.MasterData;
import com.yosua.recommendapp.model.Vertex;
import com.yosua.recommendapp.utils.DijkstraAlgorithm;
import com.yosua.recommendapp.utils.PrefConfig;
import com.yosua.recommendapp.utils.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FillDataViewModel extends AndroidViewModel {

    private List<Vertex> nodes;
    private List<Edge> edges;
    private IFillDataCallback callback;
    private DatabaseReference dbRef;
    private String projectName;
    private PrefConfig prefConfig;

    public void setPrefConfig(PrefConfig prefConfig) {
        this.prefConfig = prefConfig;
    }

    public void setCallback(IFillDataCallback callback) {
        this.callback = callback;
    }

    public FillDataViewModel(@NonNull Application application) {
        super(application);
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void checkForResult(List<MasterData> masterDataList, String projectName) {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

        this.projectName = projectName;

        int pointCounter = -1;

        for (int i = 0; i < masterDataList.size(); i++) {
            MasterData masterData = masterDataList.get(i);
            List<Data> dataList = masterData.getDataList();

            for (int j = 0; j < dataList.size(); j++) {
                pointCounter++;
                if (i == 0 || i == masterDataList.size() - 1) {
                    String id = getSmallestAmount(i, masterData.getDataList());
                    Vertex location = new Vertex(id, pointCounter);
                    nodes.add(location);
                    break;
                }

                String id = "Node_" + i + "_" + j;
                Vertex location = new Vertex(id, pointCounter);
                nodes.add(location);
            }
        }

        int counter = 0;

        for (int i = 0; i < nodes.size(); i++) {
            String nodeID = nodes.get(i).getId();
            String[] splitNode = nodeID.split("_");

            int pagePosition = Integer.parseInt(splitNode[1]);
            int dataPosition = Integer.parseInt(splitNode[2]);

            for (int j = 0; j < nodes.size(); j++) {
                String nodeIDs = nodes.get(j).getId();
                String[] splitNodes = nodeIDs.split("_");

                int pagePositions = Integer.parseInt(splitNodes[1]);
                int dataPositions = Integer.parseInt(splitNodes[2]);

                if (pagePositions == (pagePosition + 1)) {
                    Data data = masterDataList.get(pagePosition).getDataList().get(dataPosition);
                    Data data1 = masterDataList.get(pagePosition).getDataList().get(dataPosition);
                    double distance = (data.getPrice() + data1.getPrice()) / 2;
                    // TODO: Uncommend this
//                    double distance = (data.getPrice() * (Constant.MAX_RATE - data.getRate()) + data1.getPrice() * (Constant.MAX_RATE - data1.getRate())) / 2;
                    addLane("Edge_" + counter, nodes.get(i), nodes.get(j), distance);
                    counter++;
                }
            }
        }

        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(nodes.size() - 1));

//        assertNotNull(path);
//        assertTrue(path.size() > 0);

        List<Data> dataList = new ArrayList<>();

        for (Vertex vertex : path) {
            String nodeID = vertex.getId();
            String[] splitNode = nodeID.split("_");
            int pagePosition = Integer.parseInt(splitNode[1]);
            int dataPosition = Integer.parseInt(splitNode[2]);
            Data data = masterDataList.get(pagePosition).getDataList().get(dataPosition);
            dataList.add(data);
        }

        sendToDatabase(dataList);
    }

    private String getSmallestAmount(int pos, List<Data> dataList) {
        String nodeID = "";
        double minValue = 0;
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);
            // TODO: Uncommend this
//            double value = data.getPrice() * (Constant.MAX_RATE - data.getRate());
            double value = data.getPrice();
            if (i == 0) {
                minValue = value;
                nodeID = "Node_" + pos + "_" + i;
            } else if (minValue > value) {
                minValue = value;
                nodeID = "Node_" + pos + "_" + i;
            }
        }
        return nodeID;
    }

    private void addLane(String laneId, Vertex sourceLocNo, Vertex destLocNo,
                         double duration) {
        Edge lane = new Edge(laneId, sourceLocNo, destLocNo, duration);
        edges.add(lane);
    }

    private void sendToDatabase(List<Data> dataList) {
        String path = Tree.HISTORY + "/" + prefConfig.getUID();
        String projectID = dbRef.child(path).push().getKey();
        path += "/" + projectID;
        Map<String, Object> map = new HashMap<>();
        map.put("project_name", projectName);
        map.put("project_id", projectID);
        map.put("data", dataList);

        dbRef.child(path)
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onLoadData(dataList);
                        } else {
                            callback.onFailed("There is something error, please try again later");
                        }
                    }
                });
    }
}
