
import sys
import csv
import numpy as np
import math


def read_file(file_name):

    training_data=list(csv.reader(open(file_name)))
    number_of_attribute=len(training_data[0])
    data=[]
    for line in training_data:
        temp_data=[]
        for i in range(number_of_attribute-1):
            temp_data.append(float(line[i]))
        temp_data.append(line[-1])
        data.append(temp_data)
    return data

##NB############################################################################
#seperate whole data array into 2 array according yes or no to list
def seperateClass(data):
    class_no = []
    class_yes = []
    for line in data:
        if line[len(line)-1] == "no":
            class_no.append(line)
        else:
            class_yes.append(line)

    return class_yes, class_no

#calculate mean of attributes specified use its index
def get_mean(datalist, attr_idx):
    sum_data = 0;
    num = len(datalist)
    for line in datalist:
            sum_data += line[attr_idx]
    mean = sum_data/num

    return mean

#calculate standard deviation of attributes specified use its index
def sd(datalist, attr_idx):
    sum_diff_sqr = 0
    for line in datalist:
        sum_diff_sqr += math.pow(line[attr_idx] - get_mean(datalist, attr_idx), 2)
    sdv = math.sqrt(sum_diff_sqr/(len(datalist)-1))

    return sdv


#calculate the prob density of numerical data in NB
def probability_density(mean, sdv, x):
    prob = (1/(sdv*math.sqrt(2*math.pi))) * math.exp(-math.pow(x - mean,2)/(2*math.pow(sdv,2)))
    return prob


#put means and sdvs in list type since we have more than one testing case
def get_mean_sdv_list(listdata):
    ls_mean = []
    ls_sdv = []
    length = len(listdata[0])
    for i in range(0,length-1):
        mean = get_mean(listdata, i)
        ls_mean.append(mean)
        sdv = sd(listdata, i)
        ls_sdv.append(sdv)
    return ls_mean, ls_sdv


#the NB algo
def NB(training_dataset, testing_dataset):
    num_of_attri = len(testing_dataset[0])
    train_yes, train_no = seperateClass(training_dataset)
    #print(train_yes)

    train_yes_mean, train_yes_sd = get_mean_sdv_list(train_yes)
    train_no_mean, train_no_sd = get_mean_sdv_list(train_no)

    #print(train_no_sd)


    #calculate yes and no with each evidence
    row= 0
    while row < len(testing_dataset):
        prob_yes = len(train_yes)/len(training_dataset)
        prob_no =  1 - prob_yes
        #print("row:",row)
        for j in range(0,num_of_attri):
            pj_yes = probability_density(train_yes_mean[j],train_yes_sd[j],float(testing_dataset[row][j]))
            #print(prob_yes,"each lel: ",pj_yes)
            prob_yes *= pj_yes
            #print("after muti: ",prob_yes)

            pj_no = probability_density(train_no_mean[j],train_no_sd[j],float(testing_dataset[row][j]))
            prob_no *= pj_no

    #compare the result and append class for testing
        #print(prob_yes)
        #print(prob_no)
        #print("")
        if prob_yes > prob_no:
            testing_dataset[row].append("yes")
        elif prob_yes <= prob_no:
            testing_dataset[row].append("no")
        #print("finish")
        row += 1

    return testing_dataset
###################################################################################################


##KNN##############################################################################################
#step 1: Calculate Euclidean distance
def Euclidean_d(training_dt, testing_dt):
    distance = 0
    for i in range(0,len(training_dt)-1):

        distance += math.pow((training_dt[i]-float(testing_dt[i])),2)

    distance=math.sqrt(distance)
    return distance

#step 2: Get Nearest Neighbors
def get_neighbors(training_dataset,testing_dt,k):

    distances=list()
    for train_dt in training_dataset:
        distance=Euclidean_d(train_dt, testing_dt)
        distances.append((train_dt,distance))

    distances.sort(key=lambda tup:tup[1])

    neighbors=list()
    for i in range(k):
        neighbors.append(distances[i][0])
    return neighbors

#step 3: Make Predictions
def KNN(k, training_dataset, testing_dataset):
    for testing_dt in testing_dataset:
        neighbors=get_neighbors(training_dataset,testing_dt,k)
        predict=0
        for neighbor in neighbors:
            if neighbor[-1]=="yes":
                predict+=1
            else:
                predict-=1
        if predict>=0:
            testing_dt.append("yes")
        else:
            testing_dt.append("no")

    return testing_dataset

###################################################################################################
def write_output_file(folds):
    with open('pima-folds.csv', 'w') as f:
        writer = csv.writer(f, delimiter=',', quotechar='"')
        count = 0
        for i in range(0,len(folds)):
            count += 1
            f.write('fold{}\n'.format(count))
            for case in folds[i]:
                #for e in case:
                writer.writerow(case)

            if count < 10:
                f.write('\n')

##10_Folds###############################################################################

def seperate_folds(training_data):
    yes_list, no_list = seperateClass(training_data)

    left_yes = len(yes_list)%10
    num_of_yes = int((len(yes_list) - left_yes) / 10)

    left_no = len(no_list)%10
    num_of_no = int((len(no_list) - left_no) / 10)
    # print(num_of_no, left_no)
    # print(num_of_yes, left_yes)

    seperated_yes = []
    seperated_no = []
    ten_folds = []

    idxy = 0
    while idxy < len(yes_list)-1:
        new_position = idxy + num_of_yes
        if(new_position < len(yes_list)-1):
            seperated_yes.append(yes_list[idxy : new_position])
            idxy = new_position
        else:
            left_list = yes_list[idxy : len(yes_list)]
            break

    idx = 0
    for i in range(0,len(left_list)):
        seperated_yes[i].append(left_list[idx])
        idx += 1



    idxn = 0
    while idxn < len(no_list)-1:
        new_position = idxn + num_of_no
        if(new_position < len(no_list)-1):
            seperated_no.append(no_list[idxn : new_position])
            idxn = new_position
        else:
            seperated_no.append(no_list[idxn : len(no_list)])
            break

    for i in range(0,len(seperated_yes)):
        fold = []
        fold = seperated_yes[i] + seperated_no[i]
        ten_folds.append(fold)

    return ten_folds




#################################################################################
def main(argv):
    #argv = ['pima.csv', 'pima_test.csv', 'NB']
    result=[]
    #Input
    training_file = argv[0]
    testing_file = argv[1]
    algo = argv[2]
    #Read contents of training dataset and testing dataset
    training_dataset = read_file(training_file)
    testing_dataset = read_file(testing_file)

    #Different algorithm
    if algo == "NB":
        result = NB(training_dataset, testing_dataset)

    elif "NN" in algo:
        k = int(algo[0])
        result=KNN(k,training_dataset,testing_dataset)

    for i in result:
       print(i[-1])

    folds = seperate_folds(training_dataset)
    #write_output_file(folds)


if __name__ == "__main__":
    main(sys.argv[1:])
