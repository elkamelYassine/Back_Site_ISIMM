import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './etudiant.reducer';

export const EtudiantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const etudiantEntity = useAppSelector(state => state.etudiant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="etudiantDetailsHeading">
          <Translate contentKey="isimmManagerApp.etudiant.detail.title">Etudiant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.id}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="isimmManagerApp.etudiant.prenom">Prenom</Translate>
            </span>
            <UncontrolledTooltip target="prenom">
              <Translate contentKey="isimmManagerApp.etudiant.help.prenom" />
            </UncontrolledTooltip>
          </dt>
          <dd>{etudiantEntity.prenom}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="isimmManagerApp.etudiant.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.nom}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="isimmManagerApp.etudiant.email">Email</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.email}</dd>
          <dt>
            <span id="numEtudiant">
              <Translate contentKey="isimmManagerApp.etudiant.numEtudiant">Num Etudiant</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.numEtudiant}</dd>
          <dt>
            <span id="numTel">
              <Translate contentKey="isimmManagerApp.etudiant.numTel">Num Tel</Translate>
            </span>
          </dt>
          <dd>{etudiantEntity.numTel}</dd>
          <dt>
            <span id="dateDeNaissance">
              <Translate contentKey="isimmManagerApp.etudiant.dateDeNaissance">Date De Naissance</Translate>
            </span>
          </dt>
          <dd>
            {etudiantEntity.dateDeNaissance ? (
              <TextFormat value={etudiantEntity.dateDeNaissance} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="photoDeProfile">
              <Translate contentKey="isimmManagerApp.etudiant.photoDeProfile">Photo De Profile</Translate>
            </span>
          </dt>
          <dd>
            {etudiantEntity.photoDeProfile ? (
              <div>
                {etudiantEntity.photoDeProfileContentType ? (
                  <a onClick={openFile(etudiantEntity.photoDeProfileContentType, etudiantEntity.photoDeProfile)}>
                    <img
                      src={`data:${etudiantEntity.photoDeProfileContentType};base64,${etudiantEntity.photoDeProfile}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {etudiantEntity.photoDeProfileContentType}, {byteSize(etudiantEntity.photoDeProfile)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="isimmManagerApp.etudiant.niveau">Niveau</Translate>
          </dt>
          <dd>{etudiantEntity.niveau ? etudiantEntity.niveau.id : ''}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.etudiant.club">Club</Translate>
          </dt>
          <dd>
            {etudiantEntity.clubs
              ? etudiantEntity.clubs.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {etudiantEntity.clubs && i === etudiantEntity.clubs.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/etudiant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/etudiant/${etudiantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EtudiantDetail;
