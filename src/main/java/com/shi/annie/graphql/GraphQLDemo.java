package com.shi.annie.graphql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shi.annie.domain.Person;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;

import graphql.Scalars;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.ExecutorServiceExecutionStrategy;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.execution.batched.BatchedExecutionStrategy;
import graphql.language.FieldDefinition;
import graphql.schema.*;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static com.shi.annie.domain.Person.getPerson;
import static com.shi.annie.domain.Person.getPersonList;
import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.*;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/1/18 11:27 上午
 */
public class GraphQLDemo {

    private static GraphQLObjectType personAPI;
    private static  GraphQLInputType personInput ;
    private static GraphQLObjectType queryType;
    private static GraphQLObjectType mutationType;
    private static  GraphQLSchema personSchema;

    public static void main(String[] args) {
        //HelloWorldTest();
        initGraphQL();
        //POJOTest();
        AsyncTest();

    }

    //hello world graphql
    public static void HelloWorldTest() {
        //构建graphql
        String schema = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schema);  //类型定义
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()   //数据
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        //发起查询
        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{hello}");
        System.out.println(executionResult.getData().toString());
    }

    //pojo graphql
    private static void POJOTest() {

        //发起查询
        GraphQL build = GraphQL.newGraphQL(personSchema)
                .queryExecutionStrategy(new AsyncExecutionStrategy())    //默认执行策略，该策略不会按字段顺序来获取数据，但是查询结果一定会按字段顺序返回，对于查询，推荐该策略
                .mutationExecutionStrategy(new AsyncExecutionStrategy())
//                .queryExecutionStrategy(new AsyncSerialExecutionStrategy())    //异步顺序执行策略，对于mutation操作，使用该策略保证字段的串行更新
//                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy())
                //.queryExecutionStrategy(new ExecutorServiceExecutionStrategy(xxxExecutorService))   //自定义执行器，
                //.queryExecutionStrategy(new SubscriptionExecutionStrategy())    //订阅查询策略
                //.queryExecutionStrategy(new BatchedExecutionStrategy())    //批量化执行策略，对于大量List数据可以用该策略批量化
                .build();

        //根据id查询
        ExecutionResult result = build.execute("query{personQuery(id:12){name,age,sex,birthday}}");
        System.out.println(result.getData().toString());
        //查询用户
        result = build.execute("query{personList{name,age,sex,birthday}}");
        System.out.println(result.getData().toString());
        //新增用户
        result = build.execute("mutation{addPersonList(personList:[{name:\"石傻傻\",age:12,sex:true,birthday:\"2020-01-17 14:11:05\"}]){name,age,sex,birthday}}");
        System.out.println(result.getData().toString());

    }
    //异步
    private static void AsyncTest() {
        GraphQL build = GraphQL.newGraphQL(personSchema)
                .queryExecutionStrategy(new AsyncExecutionStrategy())
                .mutationExecutionStrategy(new AsyncExecutionStrategy()).build();
        //异步query and mutation
        ExecutionInput queryInput = ExecutionInput.newExecutionInput()
                .query("query{personQuery(id:12){name,age,sex,birthday}}")
                .build();
        CompletableFuture<ExecutionResult> promise = build.executeAsync(queryInput);
        promise.thenAccept(resultAsync -> System.out.println(resultAsync.getData().toString()));

        promise.join(); //同步执行

//        DataFetcher userDataFetcher = new DataFetcher() {
//            @Override
//            public Object get(DataFetchingEnvironment environment) {
//                CompletableFuture<User> userPromise = CompletableFuture.supplyAsync(() -> {
//                    return fetchUserViaHttp(environment.getArgument("userId"));
//                });
//                return userPromise;
//            }
//        };
    }
    //订阅
    private static void SubscribeTest() {
        GraphQL build = GraphQL.newGraphQL(personSchema)
                .queryExecutionStrategy(new SubscriptionExecutionStrategy())
                .mutationExecutionStrategy(new SubscriptionExecutionStrategy()).build();
        ExecutionResult executionResult = build.execute("query{personQuery(id:12){name,age,sex,birthday}}");
        Publisher<ExecutionResult> personStream = executionResult.getData();
        AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();
        personStream.subscribe(new Subscriber<ExecutionResult>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscriptionRef.set(subscription);
                subscription.request(1L);
            }

            @Override
            public void onNext(ExecutionResult executionResult) {
                System.out.println(executionResult.getData().toString());
                subscriptionRef.get().request(1L);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //构建pojo schema (手动)
    private static void initGraphQL() {
        //定义GraphQL类型
        personAPI = GraphQLObjectType.newObject()
                .name("personAPI")
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("age").type(GraphQLInt))
                .field(newFieldDefinition().name("sex").type(GraphQLBoolean))
                .field(newFieldDefinition().name("birthday").type(GraphQLString))
                .build();
        personInput = newInputObject()   //mutation 入参
                .name("personInput")
                .field(newInputObjectField().name("name").type(GraphQLString))
                .field(newInputObjectField().name("age").type(GraphQLInt))
                .field(newInputObjectField().name("sex").type(GraphQLBoolean))
                .field(newInputObjectField().name("birthday").type(GraphQLString))
                .build();
        //定义暴露给前端的Query API
        queryType = GraphQLObjectType.newObject()
                .name("query")
                .field(newFieldDefinition()
                        .type(personAPI)
                        .name("personQuery")  //接口名称
                        .argument(GraphQLArgument.newArgument()
                                .name("id")
                                .type(new GraphQLNonNull(GraphQLInt)))
                        .dataFetcher(environment -> {
                            System.out.println("接收到参数: " + environment.getArgument("id"));
                            return getPerson();
                        }))
                .field(newFieldDefinition()
                        .type(new GraphQLList(personAPI))
                        .name("personList")
                        .dataFetcher(environment -> getPersonList())
                )
                .build();
        //定义暴露给前端的Mutation API
        mutationType = GraphQLObjectType.newObject()
                .name("mutation")
                .field(newFieldDefinition()
                        .type(new GraphQLList(personAPI))
                        .name("addPersonList")  //接口名称
                        .argument(GraphQLArgument.newArgument()
                                .name("personList")
                                .type(new GraphQLNonNull(new GraphQLList(personInput))))
                        .dataFetcher(environment -> {
                            List<Person> personList = getPersonList();
                            System.out.println("接收到参数: " + environment.getArgument("personList"));
                            List<Map<String, Object>> personMapList = environment.getArgument("personList");
                            for (Map<String, Object> personMap : personMapList) {
                                Person person = JSON.parseObject(JSON.toJSONString(personMap), Person.class);
                                personList.add(person);
                            }
                            return personList;
                        }))
                .build();
        personSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .mutation(mutationType)
                .build();
    }

}
